package ru.andreya108.dumcharmarkupinfo;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrey on 30.09.2015.
 */
public class DumcharUtil
{
    public static final String PROC_DUMCHAR_INFO = "/proc/dumchar_info";
    public static final long KB = 1024;
    public static final long MB = KB*1024;
    public static final long GB = MB*1024;
    public static final long STOCK_BOOTIMG_SIZE = 6*MB;
    public static final long PLUS_BOOTIMG_SIZE = 16*MB;

    private final Resources resources;
    private String markup = null;
    private ArrayList<PartitionSizeInfo> partSizes;
    private long emmcSizeGb = 0;
    private Context context;

    class DumcharId {
        public static final String PRELOAD = "preload";
        public static final String BOOTIMG = "bootimg";
    }

    class DumcharEntry
    {
        String id;
        long offset;
        long size;
        int type;
        String dev;

        public DumcharEntry(String id, String size_hex, String offset_hex, String type, String dev) {
            this.id = id;
            this.dev = dev;
            this.size = Long.parseLong(size_hex, 16);
            this.offset = Long.parseLong(offset_hex, 16);
            this.type = Integer.parseInt(type);
        }
    }

    String dumcharInfo = null;
    Map<String, DumcharEntry> entries = new HashMap<>();

    public DumcharUtil(Context context)
    {
        this.context = context;
        this.resources = context.getResources();
        partSizes = new ArrayList<>();
    }

    public boolean readDumchar() {
        boolean result = false;
        File dumchar = new File(PROC_DUMCHAR_INFO);

        String entry = "^(\\w+)\\s+0x([\\d\\w]+)\\s+0x([\\d\\w]+)\\s+(\\d+)\\s+(.+)";
        Pattern p = Pattern.compile(entry);

        long sumSize = 0;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dumchar)));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {

                Matcher m = p.matcher(line);
                if (m.matches()) {
                    String id = m.group(1);
                    entries.put(id, new DumcharEntry(id,m.group(2),m.group(3),m.group(4),m.group(5)));
                    sumSize += entries.get(id).size;

                    sb.append( line.replace("0x00000", "") ).append("\n");
                }
            }
            reader.close();
            dumcharInfo = sb.toString();

            markup = detectMarkup();

            renderPartInfo();

            emmcSizeGb = detectEmmcSize(sumSize);

            result = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private long detectEmmcSize(long sumSize) {
        return sumSize/GB + 1;
    }

    private String detectMarkup() {

        StringBuilder sb = new StringBuilder();

        if ( entries.get(DumcharId.PRELOAD) == null )
        {
            sb.append( resources.getString(R.string.markup_cn) );
        }
        else
        {
            sb.append( resources.getString(R.string.markup_row) );
        }

        DumcharEntry bootimg = entries.get(DumcharId.BOOTIMG);
        if (bootimg.size == resources.getInteger(R.integer.plus_bootimg_size))
        {
            sb.append( resources.getString(R.string.markup_plus) );
        }
        else if (bootimg.size != resources.getInteger(R.integer.stock_bootimg_size))
        {
            sb.append( resources.getString(R.string.markup_unknown) );
        }

       return sb.toString();
    }

    private void renderPartInfo() {

        partSizes.clear();

        String[] parts = resources.getStringArray(R.array.part_info);

        for (int i=0; i<parts.length; i++)
        {
            DumcharEntry part = entries.get(parts[i]);
            if (part != null) {

                int id = resources.getIdentifier("label_" + part.id, "string", context.getPackageName());

                partSizes.add(new PartitionSizeInfo(resources.getString(id), part.size));
            }
        }
    }

    private String formatPartInfoStr(String id, long size)
    {
        return String.format("%-14s  %10d Mb", id, size/MB);
    }

    public String getDumcharInfo()
    {
        return dumcharInfo;
    }

    public String getMarkup()
    {
        return markup;
    }

    public long getPartitionSize(String id)
    {
        DumcharEntry entry = entries.get(id);
        return entry == null ? 0 : entry.size;
    }

    public ArrayList<PartitionSizeInfo> getPartSizes() {
        return partSizes;
    }

    public long getEmmcSizeGb()
    {
        return emmcSizeGb;
    }
}

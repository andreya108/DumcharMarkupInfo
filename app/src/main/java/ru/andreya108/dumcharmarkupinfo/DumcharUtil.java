package ru.andreya108.dumcharmarkupinfo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andrey on 30.09.2015.
 */
public class DumcharUtil
{
    private String markup = null;
    private String partSizes = null;
    private long emmcSizeGb = 0;

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

    public static final long KB = 1024;
    public static final long MB = KB*1024;
    public static final long GB = MB*1024;
    public static final long STOCK_BOOTIMG_SIZE = 6*MB;
    public static final long PLUS_BOOTIMG_SIZE = 16*MB;

    public DumcharUtil()
    {
        readDumchar();
    }

    private void readDumchar() {
        File dumchar = new File("/proc/dumchar_info");

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

            sb = new StringBuilder();
            if ( entries.get("preload") == null ) {
                sb.append("CN");
            } else {
                sb.append("ROW");
            }

            DumcharEntry bootimg = entries.get("bootimg");
            if (bootimg.size == PLUS_BOOTIMG_SIZE) {
                sb.append("+");
            } else if (bootimg.size != STOCK_BOOTIMG_SIZE) {
                sb.append("?");
            }

            markup = sb.toString();

            sb = new StringBuilder();
            sb.append(formatPartInfoStr("/system", entries.get("android").size));
            sb.append("\n");
            sb.append(formatPartInfoStr("/data", entries.get("usrdata").size));
            DumcharEntry fat = entries.get("fat");
            if (fat != null) {
                sb.append("\n");
                sb.append(formatPartInfoStr("emmc@fat", fat.size));
            }

            partSizes = sb.toString();

            emmcSizeGb = sumSize/GB + 1;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    public String getPartSizes() {
        return partSizes;
    }

    public long getEmmcSizeGb()
    {
        return emmcSizeGb;
    }
}

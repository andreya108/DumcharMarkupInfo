package ru.andreya108.dumcharmarkupinfo;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrey on 26.10.2015.
 */
public class MmcUtil {
    public static final String MMC0_DIR = "/sys/devices/platform/mtk-msdc.0/mmc_host/mmc0/mmc0:0001";
    public static final String MMC_CID = MMC0_DIR + "/cid";
    public static final String MMC_EA_SIZE = MMC0_DIR + "/enhanced_area_size";
    public static final String MMC_NAME = MMC0_DIR + "/name";
    public static final String MMC_DATE = MMC0_DIR + "/date";
    public static final String MMC_SERIAL = MMC0_DIR + "/serial";

    private final Resources resources;
    private Context context;
/*
    public MmcChip[] chips = {
            new MmcChip("90014A483847326404", "Hynix",    "H9TP65A8JDACPR_KGM",        8,  7468,  7634944),
            new MmcChip("90014A2058494E5948", "Hynix",    "H9TP32A8JDMCPR_KGM",        4,  3702,  3784704),
            new MmcChip("90014A484247346504", "Hynix",    "H9TP26A8JDACNR_KGM",       32, 29830, 30539776),
            new MmcChip("90014A483447316404", "Hynix",    "H9TP32A8JDACPR_KGM",        4,  3702,  3784704),
            new MmcChip("90014A483447316404", "Hynix",    "H9TP32A4GDBCPR_KGM",        4,  3728,  3784704),
            new MmcChip("7001004D4D43303447", "Kingston", "KE4CN2L2HA8A2A",            4,  3628,  3715072),
            new MmcChip("FE014E503058585858", "Micron",   "MT29PZZZ8D4WKFEW_18W_6D4",  4,  3728,  3817472),
            new MmcChip("1501004B375530304D", "Samsung",  "KMK7U000VM_B309",           8,  7460,  7634944),
            new MmcChip("1501004B4A5330304D", "Samsung",  "H9TP65A8JDACPR_KGM",        8,  7468,  7634944),
            new MmcChip("1501004E4A5330304D", "Samsung",  "KMNJS000ZM_B205",           4,  3728,  3815296),
            new MmcChip("1501004B355530304D", "Samsung",  "KMK5U000VM_B309",           4,  3732,  3817472),
            new MmcChip("1501004B555330304D", "Samsung",  "KMKUS000VM_B410",           8,  7460,  7634944),
            new MmcChip("1501004B335530304D", "Samsung",  "KMK3U000VM_B410",          16, 14914, 15267840),
            new MmcChip("1501004B4A5330304D", "Samsung",  "KMKJS000VM_B309",           4,  3728,  3815296),
            new MmcChip("150100493855303041", "Samsung",  "KMI8U000MM_B605_MMD2",     16, 14914, 15267840)
    };
*/
    Map<String, MmcChip> chipdb = new HashMap<String, MmcChip>();
    String  cid = null;
    MmcChip mmc0 = null;

    public String getCid() {
        return cid;
    }

    public MmcChip getChip() {
        return mmc0;
    }

    public MmcUtil(Resources resources)
    {
        this.resources = resources;

        String[] mmc_chips = resources.getStringArray(R.array.mmc_chips);

        for (int i=0; i<mmc_chips.length; i++)
        {
            MmcChip chip = MmcChip.newInstanceFromString(mmc_chips[i]);
            if (chip != null)
                chipdb.put(chip.cid, chip);
        }

        cid = getMmcCid();

        if (cid != null) {
            String shortCid = cid.substring(0, 18).toUpperCase();
            Log.d("MMC", "short cid = "+shortCid);

            mmc0 = chipdb.get(shortCid);
        }
    }

//    0x1501004B375530304D
//      1501004b375530304d08ea7d6e1190af
    static String getMmcCid() {
        String cid = readMmcFile(MMC_CID);
        Log.d("MMC", "cid = "+cid);

        return cid;
    }

    static String readMmcFile(String name)
    {
        String res = null;
        File file = new File(name);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            res = reader.readLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}

package ru.andreya108.dumcharmarkupinfo;

/**
 * Created by Andrey on 31.10.2015.
 */
public class MmcChip {
    public final String cid;
    public final String vendor;
    public final String part;
    public final int sizeGb;
    public final int totalSizeMb;
    public final int userSizeKb;

    public MmcChip(String cid, String vendor, String part, int sizeGb, int totalSizeMb, int userSizeKb) {
        this.cid = cid;
        this.vendor = vendor;
        this.part = part;
        this.sizeGb = sizeGb;
        this.totalSizeMb = totalSizeMb;
        this.userSizeKb = userSizeKb;
    }

    // 90014A483847326404,Hynix,H9TP65A8JDACPR_KGM,8,7468,7634944
    public static MmcChip newInstanceFromString(String mmc_chip) {
        MmcChip chip = null;

        String[] items = mmc_chip.split(",");

        if (items.length > 0) {

            chip = new MmcChip(items[0], items[1], items[2], Integer.valueOf(items[3]), Integer.valueOf(items[4]), Integer.valueOf(items[5]));
        }

        return chip;
    }
}

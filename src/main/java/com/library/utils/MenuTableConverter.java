package com.library.utils;

import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.asciithemes.a8.A8_Grids;
import de.vandermeer.skb.interfaces.document.TableRowStyle;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class MenuTableConverter {
    private static MenuTableConverter instance;
    private MenuTableConverter(){};
    public static MenuTableConverter getInstance(){
        if (instance==null) instance=new MenuTableConverter();
        return instance;
    }
    public String convertMtplCol(String... title){
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.getRenderer().setCWC(new CWC_LongestWordMin(new int[]{3,82}));
        AT_Row row =  table.addRow(null,title[0]);
        row.getCells().get(1).getContext().setTextAlignment(TextAlignment.CENTER);
        table.addRule();
        if (title.length>0){
            for(int i=1;i<title.length;i++){
                table.addRow(String.valueOf(i),title[i]);
            }
        }
        table.addRule();
        table.addRow("#", "Trở lại").getCells().get(0).getContext().setTextAlignment(TextAlignment.CENTER);
        table.addRule();
        table.getContext().setGridTheme(TA_GridThemes.HORIZONTAL);
        table.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        return table.render(100);
    }
    public String convertSingleCol(String... titles){
        AsciiTable table = new AsciiTable();
        table.addRule(TableRowStyle.HEAVY);
        for (String title : titles){
            AT_Row row = table.addRow(title);
            row.getCells().get(0).getContext().setTextAlignment(TextAlignment.CENTER);
            table.addRule(TableRowStyle.HEAVY);
        }
        table.getContext().setGrid(A8_Grids.lineDoubleBlocks());
        return  table.render(100);
    }
}

package slecon.component.table;
import java.awt.Point;

public interface GridBagModel {
    int DEFAULT = 0;
    int MERGE = 1;
    int COVERED = -1;

    /**
     * @param row
     *            琛�
     * @param column
     *            鍒�
     * @return 瑭插柈鍏冩牸鍦ㄨ銆佸垪鐨勮法搴�
     */
    Point getGrid(int row, int column);

    
    /**
     * 鍦╕杌告柟鍚戠殑璺ㄥ害
     * 
     * @param row
     * @param column
     * @return
     */
    int getRowGrid(int row, int column);

    
    /**
     * 鍦╔杌告柟鍚戠殑璺ㄥ害
     * 
     * @param row
     * @param column
     * @return
     */
    int getColumnGrid(int row, int column);

    
    /**
     * @param rows
     *            琛岄泦鍚�
     * @param columns
     *            鍒楅泦鍚�
     * @return 鍠厓鏍奸泦鍚堟槸鍚﹀彲浠ュ悎浣靛湪涓�璧�
     */
    boolean canMergeCells(int[] rows, int[] columns);

    
    /**
     * 鍒ゆ柗瑭插柈鍏冩牸鐙�鎱�
     * 
     * @param row
     * @param column
     * @return MERGE|DEFAULT|COVERED
     */
    int getCellState(int row, int column);

    
    /**
     * 灏囧柈鍏冩牸闆嗗悎鍚堜降
     * 
     * @param startRow
     *            闁嬪琛�
     * @param endRow
     *            绲愭潫琛�
     * @param startColumn
     *            闁嬪鍒�
     * @param endColumn
     *            绲愭潫鍒�
     * @return 鏄惁鍚堜降鎴愬姛
     */
    boolean mergeCells(int startRow, int endRow, int startColumn, int endColumn);

    
    /**
     * 灏囧柈鍏冩牸闆嗗悎鍚堜降
     * 
     * @param rows
     *            琛岄泦鍚�
     * @param columns
     *            鍒楅泦鍚�
     * @return 鏄惁鍚堜降鎴愬姛
     */
    boolean mergeCells(int[] rows, int[] columns);

    
    /**
     * 鎷嗗垎鍠厓鏍�
     * 
     * @param row
     *            琛�
     * @param column
     *            鍒�
     * @return 鏄惁鎷嗗垎鎴愬姛
     */
    boolean spliteCellAt(int row, int column);

    
    /**
     * 娓呴櫎 鎵�鏈夊悎浣�
     */
    void clearMergence();
}  

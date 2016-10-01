package view.dialogLoader;

public class Maze3DFormDetailes {
	
	private String name;
	private Integer levels;
	private Integer rows;
	private Integer columns;

	public Maze3DFormDetailes(String name, Integer levels, Integer rows, Integer columns) {
		this.name = name;
		this.levels = levels;
		this.rows = rows;
		this.columns = columns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevels() {
		return levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	



}
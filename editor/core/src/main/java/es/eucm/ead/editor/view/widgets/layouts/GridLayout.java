/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.eucm.ead.editor.view.widgets.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;

/**
 * A layout that displays it's children in a grid. Has an {@link Array} of
 * {@link Cell} cell that positions their actors. It's used by the
 * {@link DraggableGridLayout} in order to be able to drag and drop and position
 * it's children. This layout can increase it's number of rows or columns at any
 * moment.
 * 
 * The cells are positioned by the following order: top-left first
 * 
 * <pre>
 *     _____ _____ _____
 *    |     |     |     |
 *    |  0  |  1  |  2  |
 *    |_____|_____|_____|
 *    |     |     |     |
 *    |  3  |  4  |  5  |
 *    |_____|_____|_____|
 *    |     |     |     |
 *    |  6  |  7  |  8  |
 *    |_____|_____|_____|
 * 
 * </pre>
 */
public class GridLayout extends WidgetGroup {

	private static final int INITIAL_ROWS = 1;
	private static final int INITIAL_COLUMNS = 1;

	private int rows;
	private int columns;
	private Array<Cell> cells;

	/**
	 * Creates a layout with {@value #INITIAL_ROWS} rows and
	 * {@value #INITIAL_COLUMNS} columns.
	 */
	public GridLayout() {
		this(INITIAL_ROWS, INITIAL_COLUMNS);
	}

	public GridLayout(int initialRows, int initialColumns) {
		rows = initialRows;
		columns = initialColumns;
		cells = new Array<Cell>(rows * columns);
		clearCells();
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	/**
	 * Adds an actor in the first cell found empty. If there are no empty cells
	 * left a new row will be added at the end of the rows.
	 */
	public Cell add(Actor actor) {
		return add(actor, true);
	}

	/**
	 * Adds an actor in the first cell found empty.
	 * 
	 * @param actor
	 * @param increaseRow
	 *            if true and there are no empty tiles left a new row will be
	 *            added at the end of the rows, else a new column will be added
	 *            at the end of the columns
	 */
	public Cell add(Actor actor, boolean increaseRow) {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				int index = i * columns + j;
				Cell cell = cells.get(index);
				if (cell.getWidget() == null) {
					cell.setWidget(actor);
					return cell;
				}
			}
		}
		if (increaseRow) {
			addRowAtTheEnd();
		} else {
			addColumnsAtTheEnd();
		}
		return add(actor, increaseRow);
	}

	@Override
	public float getPrefWidth() {
		return getTilePrefWidth() * columns;
	}

	private float getTilePrefWidth() {
		float tilePrefWidth = 0f;
		for (Cell cell : cells) {
			tilePrefWidth = Math.max(tilePrefWidth, cell.getPrefWidth());
		}
		return tilePrefWidth;
	}

	@Override
	public float getPrefHeight() {
		return getTilePrefHeight() * rows;
	}

	private float getTilePrefHeight() {
		float tilePrefHeight = 0f;
		for (Cell cell : cells) {
			tilePrefHeight = Math.max(tilePrefHeight, cell.getPrefHeight());
		}
		return tilePrefHeight;
	}

	@Override
	public void layout() {

		float prefTileWidth = getTilePrefWidth();
		float prefTileHeight = getTilePrefHeight();

		for (Cell cell : cells) {
			float x = cell.column * prefTileWidth;
			float y = getHeight() - (cell.row + 1) * prefTileHeight;
			float width = prefTileWidth;
			float height = prefTileHeight;
			cell.setBounds(Math.round(x), Math.round(y), Math.round(width),
					Math.round(height));
			cell.validate();
		}
	}

	public void addRowAtTheBegining() {
		addRowAt(0);
	}

	public void addRowAtTheEnd() {
		addRowAt(rows);
	}

	/**
	 * Adds a new empty row at a given index.
	 * 
	 * @param row
	 */
	public void addRowAt(int row) {
		++rows;
		int beginIndex = row * columns;
		for (int j = 0; j < columns; ++j) {
			int index = beginIndex + j;
			Cell newCell = new Cell(null, row, j);
			cells.insert(index, newCell);
			addActorAt(index, newCell);
		}
		for (int i = row + 1; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				Cell cell = cells.get(i * columns + j);
				cell.row = i;
				cell.column = j;
			}
		}
		invalidateHierarchy();
	}

	public void addColumnAtTheBegining() {
		addColumnAt(0);
	}

	public void addColumnsAtTheEnd() {
		addColumnAt(columns);
	}

	/**
	 * Adds a new empty column at a given index.
	 * 
	 * @param column
	 */
	public void addColumnAt(int column) {
		++columns;
		for (int i = 0; i < rows; ++i) {
			int index = i * columns + column;
			Cell newCell = new Cell(null, i, column);
			cells.insert(i * columns + column, newCell);
			addActorAt(index, newCell);
		}
		for (int i = 0; i < rows; ++i) {
			for (int j = column + 1; j < columns; ++j) {
				Cell cell = cells.get(i * columns + j);
				cell.row = i;
				cell.column = j;
			}
		}
		invalidateHierarchy();
	}

	@Override
	public void clearChildren() {
		clearCells();
		super.clearChildren();
	}

	private void clearCells() {
		cells.clear();
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				Cell cell = new Cell(null, i, j);
				cells.add(cell);
				super.addActor(cell);
			}
		}
	}

	public Cell getCellFromActor(Actor act) {
		for (Cell cell : cells) {
			if (cell.getWidget() == act) {
				return cell;
			}
		}
		return null;
	}

	public Array<Cell> getCells() {
		return cells;
	}

	/**
	 * Has a position and hold information about it's row and column. It may
	 * contain an actor. If the actor it's null the cell is considered empty by
	 * the layout.
	 */
	public static class Cell extends Container {
		private int column, row;

		public Cell() {
			this(null, 0, 0);
		}

		public Cell(Actor a, int row, int col) {
			setWidget(a);
			this.column = col;
			this.row = row;
		}

		/**
		 * @param x
		 *            point x coordinate
		 * @param y
		 *            point y coordinate
		 * @return whether the point is contained in the rectangle
		 */
		public boolean contains(float x, float y) {
			return this.getX() <= x && this.getX() + this.getWidth() >= x
					&& this.getY() <= y && this.getY() + this.getHeight() >= y;
		}

		@Override
		public String toString() {
			return "[ " + row + ", " + column + " ~> " + super.toString() + "]";
		}

		@Override
		public float getPrefWidth() {
			if (getWidget() == null) {
				return 0;
			}
			return super.getPrefWidth();
		}

		@Override
		public float getPrefHeight() {
			if (getWidget() == null) {
				return 0;
			}
			return super.getPrefHeight();
		}
	}

}
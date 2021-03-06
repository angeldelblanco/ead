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
package es.eucm.ead.editor.view.widgets.mockup.edition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.view.builders.mockup.edition.EditionWindow;
import es.eucm.ead.editor.view.widgets.mockup.buttons.ToolbarButton;
import es.eucm.ead.editor.view.widgets.mockup.edition.draw.BrushStrokes;
import es.eucm.ead.editor.view.widgets.mockup.edition.draw.BrushStrokes.Mode;
import es.eucm.ead.editor.view.widgets.mockup.panels.SamplePanel;

public class EraserComponent extends EditionComponent {

	private static final String IC_ERASER = "ic_eraser";
	private final SamplePanel samplePanel;

	/**
	 * A panel that allows erasing functionality in {@link EditionWindow}.
	 * 
	 * @param parent
	 * @param viewport
	 * @param i18n
	 * @param skin
	 */
	public EraserComponent(EditionWindow parent, Controller controller,
			Skin skin) {
		super(parent, controller, skin);

		final Label label = new Label(i18n.m("edition.tool.eraser"), skin,
				"default-opaque");
		label.setWrap(false);
		label.setAlignment(Align.center);
		label.setFontScale(0.7f);

		this.add(label).center().fillX().expandX();
		this.row();
		this.samplePanel = new SamplePanel(controller, skin, 3, false, false,
				Color.WHITE);
		this.add(this.samplePanel).expand().fill();
	}

	@Override
	protected Button createButton(Vector2 viewport, Controller controller) {
		return new ToolbarButton(viewport, skin.getDrawable(IC_ERASER),
				i18n.m("edition.eraser"), skin);
	}

	public void setBrushStrokes(BrushStrokes brushStrokes) {
		this.samplePanel.setBrushStrokes(brushStrokes);
	}

	@Override
	public void show() {
		this.samplePanel.getBrushStrokes().activateMode(Mode.ERASE);
		super.show();
	}
}

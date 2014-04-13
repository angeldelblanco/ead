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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.model.RenameMetadataObject;
import es.eucm.ead.FieldNames;
import es.eucm.ead.editor.model.Model;
import es.eucm.ead.editor.model.events.FieldEvent;
import es.eucm.ead.editor.view.builders.mockup.edition.EditionWindow;
import es.eucm.ead.editor.view.listeners.ActionForTextFieldListener;
import es.eucm.ead.editor.view.listeners.ChangeNoteFieldListener;
import es.eucm.ead.editor.view.widgets.mockup.buttons.BottomProjectMenuButton;
import es.eucm.ead.editor.view.widgets.mockup.buttons.MenuButton;
import es.eucm.ead.editor.view.widgets.mockup.buttons.MenuButton.Position;
import es.eucm.ead.editor.view.widgets.mockup.buttons.ToolbarButton;
import es.eucm.ead.engine.I18N;
import es.eucm.ead.schema.editor.components.Note;

public abstract class MoreComponent extends EditionComponent {

	private static final String IC_MORE = "ic_more",
			IC_CLONE = "ic_duplicate_scene";

	protected static final float PREF_BOTTOM_BUTTON_WIDTH = .30F;
	protected static final float PREF_BOTTOM_BUTTON_HEIGHT = .18F;
	private static final int MAX_TITLE_CARACTERS = 20;
	private static final int MAX_DESCRIPTION_CARACTERS = 200;

	private final TextField name;
	private final TextArea description;
	private ChangeNoteFieldListener noteListener;

	public MoreComponent(EditionWindow parent, final Controller controller,
			Skin skin) {
		super(parent, controller, skin);
		setModal(true);

		final I18N i18n = controller.getApplicationAssets().getI18N();
		String type = null;
		if (this instanceof MoreElementComponent) {
			type = i18n.m("element");
		} else {
			type = i18n.m("scene");
		}

		this.name = new TextField("", skin);
		this.name.setMaxLength(MAX_TITLE_CARACTERS);
		final Class<?> actionClass = getNoteActionClass();
		if (actionClass != null) {
			this.name.setTextFieldListener(new ActionForTextFieldListener(
					controller, actionClass, FieldNames.NOTE_TITLE));
		}
		final String untitled = type + " " + i18n.m("untitled");
		this.name.setMessageText(untitled);

		this.description = new TextArea("", skin);
		this.description.setMaxLength(MAX_DESCRIPTION_CARACTERS);
		if (actionClass != null) {
			this.description
					.setTextFieldListener(new ActionForTextFieldListener(
							controller, actionClass,
							FieldNames.NOTE_DESCRIPTION));
		}
		final String emptyDescription = type + " " + i18n.m("emptydescription");
		this.description.setMessageText(emptyDescription);

		final MenuButton cloneButton = new BottomProjectMenuButton(viewport,
				super.i18n.m("general.clone"), skin, IC_CLONE,
				PREF_BOTTOM_BUTTON_WIDTH, PREF_BOTTOM_BUTTON_HEIGHT,
				Position.RIGHT);

		this.add(this.name).fillX().expandX();
		this.row();
		this.add(this.description).fill().expand().center().height(300f);
		this.row();
		this.add(cloneButton);
	}

	@Override
	protected Button createButton(Vector2 viewport, Skin skin, I18N i18n) {
		return new ToolbarButton(viewport, skin.getDrawable(IC_MORE),
				i18n.m("edition.more"), skin);
	}

	/**
	 * @return the {@link RenameMetadataObject} that will be performed when the
	 *         name has changed.
	 */
	protected abstract Class<?> getNoteActionClass();

	/**
	 * @return the {@link Note} linked to the current editing scene or editing
	 *         element.
	 */
	protected abstract Note getNote(Model model);

	/**
	 * Updates the displayed title/description of the current editing scene;
	 * 
	 */
	public void initialize(Controller controller) {
		final Model model = controller.getModel();
		final Note note = getNote(model);
		if (note == null)
			return;
		this.name.setText(note.getTitle() == null ? "" : note.getTitle());
		this.description.setText(note.getDescription() == null ? "" : note
				.getDescription());
		if (this.noteListener != null) {
			model.removeListener(note, this.noteListener);
		}
		model.addFieldListener(note,
				this.noteListener = new ChangeNoteFieldListener() {
					@Override
					public void titleChanged(FieldEvent event) {
						final Object newValue = event.getValue();
						String text = null;
						if (newValue == null) {
							text = "";
						} else {
							text = newValue.toString();
						}
						MoreComponent.this.name.setText(text);
					}

					@Override
					public void descriptionChanged(FieldEvent event) {
						final Object newValue = event.getValue();
						String text = null;
						if (newValue == null) {
							text = "";
						} else {
							text = newValue.toString();
						}
						MoreComponent.this.description.setText(text);
					}
				});

	}

}

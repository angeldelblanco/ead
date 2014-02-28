/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2013 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
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
package es.eucm.ead.editor.view.builders.mockup.gallery;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.actions.ChangeView;
import es.eucm.ead.editor.model.Project;
import es.eucm.ead.editor.view.builders.mockup.camera.Picture;
import es.eucm.ead.editor.view.builders.mockup.camera.Video;
import es.eucm.ead.editor.view.widgets.mockup.ToolBar;
import es.eucm.ead.editor.view.widgets.mockup.buttons.BottomProjectMenuButton;
import es.eucm.ead.editor.view.widgets.mockup.buttons.MenuButton;
import es.eucm.ead.editor.view.widgets.mockup.buttons.MenuButton.POSITION;
import es.eucm.ead.editor.view.widgets.mockup.buttons.ProjectButton;
import es.eucm.ead.editor.view.widgets.mockup.panels.GalleryGrid;
import es.eucm.ead.editor.view.widgets.mockup.panels.HiddenPanel;
import es.eucm.ead.engine.I18N;

public class Gallery extends BaseGallery {

	public static final String NAME = "mockup_gallery";

	private static final String IC_PHOTOCAMERA = "ic_photocamera",
			IC_VIDEOCAMERA = "ic_videocamera";

	private static final float PREF_BOTTOM_BUTTON_WIDTH = .25F;
	private static final float PREF_BOTTOM_BUTTON_HEIGHT = .12F;

	private HiddenPanel filterPanel;

	private GalleryGrid<Actor> galleryTable;
	

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public Actor build(Controller controller) {
		Actor window = super.build(controller);
		this.galleryTable.setActorsToHide(top, bottom, navigation);
		return window;
	}

	protected HiddenPanel filterPanel(I18N i18n, Skin skin) {
		filterPanel = new HiddenPanel(skin);
		filterPanel.setVisible(false);

		Button applyFilter = new TextButton(
				i18n.m("general.gallery.accept-filter"), skin);
		applyFilter.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Gallery.this.filterPanel.hide();
				return false;
			}
		});

		// FIXME load real tags
		CheckBox[] tags = new CheckBox[] { new CheckBox("Almohada", skin),
				new CheckBox("Camilla", skin), new CheckBox("Doctor", skin),
				new CheckBox("Enfermera", skin), new CheckBox("Guantes", skin),
				new CheckBox("Habitación", skin),
				new CheckBox("Hospital", skin),
				new CheckBox("Quirófano", skin),
				new CheckBox("Medicamentos", skin),
				new CheckBox("Médico", skin), new CheckBox("Paciente", skin),
				new CheckBox("Vehículo", skin) };
		Table tagList = new Table(skin);
		tagList.left();
		tagList.defaults().left();
		for (int i = 0; i < tags.length; ++i) {
			tagList.add(tags[i]);
			if (i < tags.length - 1)
				tagList.row();
		}
		// END FIXME

		ScrollPane tagScroll = new ScrollPane(tagList, skin, "opaque");

		filterPanel.add(tagScroll).fill().colspan(3).left();
		filterPanel.row();
		filterPanel.add(applyFilter).colspan(3).expandX();
		return filterPanel;
	}

	@Override
	protected WidgetGroup topWidget(Vector2 viewport, I18N i18n, Skin skin,
			Controller controller) {
		Table top = (Table) super.topWidget(viewport, i18n, skin, controller);

		Button filterButton = new TextButton(i18n.m("general.gallery.filter"),
				skin);
		filterButton.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (Gallery.this.filterPanel.isVisible()) {
					Gallery.this.filterPanel.hide();
				} else {
					Gallery.this.filterPanel.show();
				}
				return false;
			}
		});

		top.add(filterButton);

		return top;
	}

	@Override
	protected WidgetGroup centerWidget(Vector2 viewport, I18N i18n, Skin skin,
			Controller controller) {

		Table centerWidget = new Table();
		
		galleryTable = new GalleryGrid<Actor>(skin, 8, 4, viewport, rootWindow);

		// FIXME (Testing Grid)
		Project project = new Project();
		for (int i = 0; i < 32; i++) {
			galleryTable.addItem(new ProjectButton(viewport, i18n,
					project, skin));
		}
		// END FIXME

		ScrollPane sp = new ScrollPane(galleryTable);
		sp.setScrollingDisabled(true, false);
		
		centerWidget.add(sp).expand().fill();
		Container wrapper = new Container(filterPanel(i18n, skin));
		wrapper.setFillParent(true);
		wrapper.right().top();
		centerWidget.addActor(wrapper);

		return centerWidget;
	}

	@Override
	protected WidgetGroup bottomWidget(Vector2 viewport, I18N i18n, Skin skin,
			Controller controller) {
		ToolBar botBar = new ToolBar(viewport, skin, 0.04f);

		MenuButton pictureButton = new BottomProjectMenuButton(viewport,
				i18n.m("general.mockup.photo"), skin, IC_PHOTOCAMERA,
				PREF_BOTTOM_BUTTON_WIDTH, PREF_BOTTOM_BUTTON_HEIGHT,
				POSITION.right, controller, ChangeView.NAME, Picture.NAME);
		MenuButton videoButton = new BottomProjectMenuButton(viewport,
				i18n.m("general.mockup.video"), skin, IC_VIDEOCAMERA,
				PREF_BOTTOM_BUTTON_WIDTH, PREF_BOTTOM_BUTTON_HEIGHT,
				POSITION.left, controller, ChangeView.NAME, Video.NAME);

		botBar.add(pictureButton).left();
		botBar.add("").expandX();
		botBar.add(videoButton).right();

		return botBar;
	}
}

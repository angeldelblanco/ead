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
package es.eucm.ead.editor.nogui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import es.eucm.ead.editor.control.Controller;
import es.eucm.ead.editor.control.Selection;
import es.eucm.ead.editor.control.Templates;
import es.eucm.ead.editor.control.actions.model.SetSelection;
import es.eucm.ead.editor.model.Model;
import es.eucm.ead.editor.nogui.actions.OpenMockGame;
import es.eucm.ead.editor.nogui.actions.OpenMockGame.Game;
import es.eucm.ead.engine.mock.MockApplication;
import es.eucm.ead.schema.entities.ModelEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public abstract class EditorGUITest {

	protected MockApplication app;

	protected NoGUIEditorDesktop editorDesktop;

	protected Controller controller;

	protected Model model;

	protected Selection selection;

	protected Stage stage;

	protected Templates templates;

	@Before
	public void setUp() {
		editorDesktop = new NoGUIEditorDesktop();
		app = new MockApplication(editorDesktop);
		controller = editorDesktop.getController();
		stage = editorDesktop.getStage();
		model = controller.getModel();
		selection = model.getSelection();
		templates = controller.getTemplates();
		controller.getCommands().pushStack();
	}

	@Test
	public void testEditorDesktop() {
		Array<Runnable> runnables = new Array<Runnable>();
		collectRunnables(runnables);
		for (Runnable runnable : runnables) {
			app.postRunnable(runnable);
		}
		app.act();
	}

	protected void click(String... actorName) {
		press(actorName);
		release(actorName);
	}

	protected void press(String... actorName) {
		inputEvent(Type.touchDown, actorName);
	}

	protected void release(String... actorName) {
		inputEvent(Type.touchUp, actorName);
	}

	protected void click(String parent, String actorName) {
		press(parent, actorName);
		release(parent, actorName);
	}

	public void setSelection(Object... selection) {
		controller.action(SetSelection.class, selection);
	}

	public Actor getActor(String... names) {
		Group parent = stage.getRoot();
		Actor actor = null;
		for (String name : names) {
			if (parent == null) {
				actor = null;
				break;
			}

			actor = parent.findActor(name);
			if (actor instanceof Group) {
				parent = (Group) actor;
			}
		}
		return actor;
	}

	private void inputEvent(Type type, String... names) {
		Actor actor = getActor(names);
		if (actor == null) {
			Gdx.app.error("EditorGUITest",
					"No actor with name " + Arrays.toString(names) + " for "
							+ type);
			return;
		}
		InputEvent inputEvent = Pools.obtain(InputEvent.class);
		inputEvent.setType(type);
		inputEvent.setButton(Buttons.LEFT);
		actor.fire(inputEvent);
		Pools.free(inputEvent);

		Gdx.app.debug("EditorGUITest",
				type + " fired in " + Arrays.toString(names));
	}

	protected void collectRunnables(Array<Runnable> runnables) {
		runnables.add(new Runnable() {
			@Override
			public void run() {
				runTest();
			}
		});
	}

	protected void openEmptyGame() {
		Game game = new Game();
		game.setGame(new ModelEntity());
		ModelEntity scene = new ModelEntity();
		for (int i = 0; i < 10; i++) {
			game.addScene("scenes/scene" + i + ".json", scene);
		}
		controller.action(OpenMockGame.class, game);
	}

	protected void runTest() {

	}
}

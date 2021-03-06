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
package es.eucm.ead.engine.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * To load objects stored in json files
 */
public class JsonLoader<T> extends
		AsynchronousAssetLoader<T, AssetLoaderParameters<T>> {

	protected GameAssets gameAssets;

	protected T object;

	protected Class<T> clazz;

	public JsonLoader(GameAssets gameAssets, Class<T> clazz) {
		super(gameAssets);
		this.gameAssets = gameAssets;
		this.clazz = clazz;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName,
			FileHandle file, AssetLoaderParameters<T> parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName,
			FileHandle file, AssetLoaderParameters<T> parameter) {
		object = gameAssets
				.fromJson(clazz == Object.class ? null : clazz, file);
	}

	@Override
	public T loadSync(AssetManager manager, String fileName, FileHandle file,
			AssetLoaderParameters<T> parameter) {
		return object;
	}
}

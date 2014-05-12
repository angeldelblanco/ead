/**
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
package es.eucm.ead.engine.systems.effects;

import ashley.core.Entity;
import es.eucm.ead.engine.Accessor;
import es.eucm.ead.engine.variables.VariablesManager;
import es.eucm.ead.schema.effects.ChangeEntityProperty;

/**
 * Executes {@link ChangeEntityProperty} effects. Example: The next effect
 * 
 * <pre>
 *     {
 *         class: changeentityproperty,
 *         property: components<velocity>.x,
 *         expression: f30
 *     }
 * </pre>
 * 
 * Accesses the velocity component in the target entity the effect is being
 * executed onto, and sets its x property to 30.
 */
public class ChangeEntityPropertyExecutor extends
		EffectExecutor<ChangeEntityProperty> {

	private Accessor accessor;

	private VariablesManager variablesManager;

	public ChangeEntityPropertyExecutor(Accessor accessor,
			VariablesManager variablesManager) {
		// this.accessor = new Accessor(null, entitiesLoader);
		this.accessor = accessor;
		this.variablesManager = variablesManager;
	}

	@Override
	public void execute(Entity owner, ChangeEntityProperty effect) {
		Object expressionValue = variablesManager.evaluateExpression(effect
				.getExpression());
		accessor.set(owner, effect.getProperty(), expressionValue);
	}
}

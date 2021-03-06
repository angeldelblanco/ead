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
package es.eucm.ead.engine.expressions.operators;

import es.eucm.ead.engine.variables.VarsContext;
import es.eucm.ead.engine.expressions.ExpressionEvaluationException;

/**
 * Math operators with two operands.
 * 
 * @author mfreire
 */
abstract class AbstractDyadicMathOperation extends AbstractMathOperation {

	public AbstractDyadicMathOperation() {
		super(2, 2);
	}

	protected abstract float operate(float a, float b)
			throws ExpressionEvaluationException;

	protected abstract int operate(int a, int b);

	@Override
	public Object evaluate(VarsContext context)
			throws ExpressionEvaluationException {

		Object a = first().evaluate(context);
		Object b = second().evaluate(context);
		boolean floatsDetected = needFloats(a.getClass(), false);
		floatsDetected = needFloats(b.getClass(), floatsDetected);
		try {
			if (!floatsDetected) {
				return operate((Integer) a, (Integer) b);
			} else {
				a = convert(a, a.getClass(), Float.class);
				b = convert(b, b.getClass(), Float.class);
				return operate((Float) a, (Float) b);
			}
		} catch (ArithmeticException ae) {
			throw new ExpressionEvaluationException("Illegal math: " + a + " "
					+ getName() + " " + b, this);
		}
	}
}

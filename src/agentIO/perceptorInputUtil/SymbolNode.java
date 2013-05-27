/*******************************************************************************
 * Copyright 2008, 2011 Hochschule Offenburg
 * Klaus Dorer, Mathias Ehret, Stefan Glaser, Thomas Huber, Fabian Korak,
 * Simon Raffeiner, Srinivasa Ragavan, Thomas Rinklin,
 * Joachim Schilling, Ingo Schindler, Rajit Shahi, Bjoern Weiler
 *
 * This file is part of magmaOffenburg.
 *
 * magmaOffenburg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * magmaOffenburg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with magmaOffenburg. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package agentIO.perceptorInputUtil;

/**
 * Represents a Symbol Nodes, containing other child nodes (or leafs).
 * 
 * @author Simon Raffeiner
 */
public class SymbolNode
{
	/** Children list */
	public final Object[] children;

	/**
	 * Constructor
	 */
	public SymbolNode()
	{
		children = null;
	}

	/**
	 * Assignment Constructor
	 * 
	 * @param children Children list
	 */
	public SymbolNode(Object[] children)
	{
		this.children = children;
	}

	/**
	 * Returns a textual representation of the Node. If your Symbol Tree was
	 * parsed from an input string as specified by the RoboCup Server Manual,
	 * this output should always be identic to your input.
	 */
	@Override
	public String toString()
	{
		String ret = "";

		if (children == null || children.length == 0)
			return "";

		for (int i = 0; i < children.length; i++) {
			Object child = children[i];

			if (i > 0)
				ret += " ";

			if (child instanceof SymbolNode)
				ret += "(" + child.toString() + ")";
			else
				ret += child.toString();
		}

		return ret;
	}
}

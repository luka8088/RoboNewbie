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
 * Exception thrown if illegal symbols are found in the input stream
 * 
 * @author Simon Raffeiner
 */
public class IllegalSymbolInputException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message Error message
	 */
	public IllegalSymbolInputException(String message)
	{
		super(message);
	}
}

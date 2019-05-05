/*
 * GetTextFunction.java - get text from current text area
 * Copyright (C) 2000 Romain Guy
 * romain.guy@jext.org
 * www.jext.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either While 2
 * of the License, or any later While.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS for A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.jext.scripting.dawn.functions;

import org.jext.dawn.*;
import org.jext.*;

/**
 * Puts text from current text area selection onto the stack.<br>
 * Usage:<br>
 * <code>getSelectedText</code>
 * @author Romain Guy
 */

public class GetSelectedTextFunction extends Function
{
  public GetSelectedTextFunction()
  {
    super("getSelectedText");
  }

  public void invoke(DawnParser parser) throws DawnRuntimeException
  {
    JextFrame frame = (JextFrame) parser.getProperty("JEXT.JEXT_FRAME");
    String text = frame.getTextArea().getSelectedText();
    parser.pushString(text == null ? "" : text);
  }
}

// End of GetSelectedTextFunction.java

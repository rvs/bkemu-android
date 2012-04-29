/*
 * Created: 12.04.2012
 *
 * Copyright (C) 2012 Victor Antonovich (v.antonovich@gmail.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package su.comp.bk.arch.cpu.opcode;

import su.comp.bk.arch.cpu.Cpu;

/**
 * Branch if greater than or equal (N^V = 0) opcode.
 */
public class BgeOpcode extends BranchOpcode {

    public final static int OPCODE = 02000;

    public BgeOpcode(Cpu cpu) {
        super(cpu);
    }

    @Override
    public int getOpcode() {
        return OPCODE;
    }

    @Override
    protected boolean isBranchCondition(int psw) {
        return !(((psw & Cpu.PSW_FLAG_N)) != 0 ^ ((psw & Cpu.PSW_FLAG_V) != 0));
    }

}

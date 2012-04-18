/*
 * Created: 31.03.2012
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
package su.comp.bk.arch.cpu.addressing;

import su.comp.bk.arch.cpu.Cpu;

/**
 * Register deferred addressing mode: (Rn).
 * Rn contains the address of the operand.
 */
public class RegisterDeferredAddressingMode extends BaseAddressingMode {

    public final static int CODE = 1;

    public RegisterDeferredAddressingMode(Cpu cpu) {
        super(cpu);
    }

    @Override
    public int getCode() {
        return CODE;
    }

    @Override
    public int getAddress(int register) {
        return cpu.readRegister(false, register);
    }

}

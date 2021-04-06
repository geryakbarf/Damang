/*  Copyright (C) 2020-2021 Yukai Li

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package unikom.gery.damang.service.devices.lefun.requests;

import unikom.gery.damang.devices.lefun.LefunConstants;
import unikom.gery.damang.devices.lefun.commands.SetLanguageCommand;
import unikom.gery.damang.service.btle.TransactionBuilder;
import unikom.gery.damang.service.devices.lefun.LefunDeviceSupport;
import unikom.gery.damang.service.devices.miband.operations.OperationStatus;

public class SetLanguageRequest extends Request {
    private byte language;

    public SetLanguageRequest(LefunDeviceSupport support, TransactionBuilder builder) {
        super(support, builder);
    }

    public byte getLanguage() {
        return language;
    }

    public void setLanguage(byte language) {
        this.language = language;
    }

    @Override
    public byte[] createRequest() {
        SetLanguageCommand cmd = new SetLanguageCommand();

        cmd.setLanguage(language);

        return cmd.serialize();
    }

    @Override
    public void handleResponse(byte[] data) {
        SetLanguageCommand cmd = new SetLanguageCommand();
        cmd.deserialize(data);
        if (!cmd.isSetSuccess())
            reportFailure("Could not set language");

        operationStatus = OperationStatus.FINISHED;
    }

    @Override
    public int getCommandId() {
        return LefunConstants.CMD_LANGUAGE;
    }
}

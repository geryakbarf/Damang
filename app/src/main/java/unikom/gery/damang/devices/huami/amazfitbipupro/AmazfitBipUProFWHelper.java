/*  Copyright (C) 2017-2021 Andreas Shimokawa, Carsten Pfeiffer, DanialHanif

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
package unikom.gery.damang.devices.huami.amazfitbipupro;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;

import unikom.gery.damang.devices.huami.HuamiFWHelper;
import unikom.gery.damang.service.devices.huami.amazfitbipupro.AmazfitBipUProFirmwareInfo;

public class AmazfitBipUProFWHelper extends HuamiFWHelper {

    public AmazfitBipUProFWHelper(Uri uri, Context context) throws IOException {
        super(uri, context);
    }

    @Override
    protected void determineFirmwareInfo(byte[] wholeFirmwareBytes) {
        firmwareInfo = new AmazfitBipUProFirmwareInfo(wholeFirmwareBytes);
        if (!firmwareInfo.isHeaderValid()) {
            throw new IllegalArgumentException("Not a an Amazfit Bip U Pro firmware");
        }
    }
}

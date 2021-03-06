/*  Copyright (C) 2020-2021 Daniel Dakhno

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
package unikom.gery.damang.service.devices.qhybrid.requests.fossil.notification;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import unikom.gery.damang.service.devices.qhybrid.adapter.fossil.FossilWatchAdapter;

public class PlayCallNotificationRequest extends PlayNotificationRequest {
    private final static int MESSAGE_ID_CALL = 1;

    public PlayCallNotificationRequest(String number, boolean callStart, FossilWatchAdapter adapter) {
        super(callStart ? 1 : 7, callStart ? 0b00011000 : 2,
                ByteBuffer.wrap(new byte[]{(byte) 0x80, (byte) 0x00, (byte) 0x59, (byte) 0xB7}).order(ByteOrder.LITTLE_ENDIAN).getInt(),
                number, "Incoming Call", MESSAGE_ID_CALL, adapter);
    }
}

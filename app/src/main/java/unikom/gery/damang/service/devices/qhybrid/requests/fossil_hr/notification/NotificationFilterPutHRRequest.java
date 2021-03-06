/*  Copyright (C) 2019-2021 Andreas Shimokawa, Daniel Dakhno

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
package unikom.gery.damang.service.devices.qhybrid.requests.fossil_hr.notification;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import unikom.gery.damang.devices.qhybrid.NotificationHRConfiguration;
import unikom.gery.damang.service.devices.qhybrid.adapter.fossil.FossilWatchAdapter;
import unikom.gery.damang.service.devices.qhybrid.file.FileHandle;
import unikom.gery.damang.service.devices.qhybrid.requests.fossil.file.FilePutRequest;
import unikom.gery.damang.util.StringUtils;

public class NotificationFilterPutHRRequest extends FilePutRequest {
    public NotificationFilterPutHRRequest(NotificationHRConfiguration[] configs, FossilWatchAdapter adapter) {
        super(FileHandle.NOTIFICATION_FILTER, createFile(configs), adapter);
    }


    public NotificationFilterPutHRRequest(ArrayList<NotificationHRConfiguration> configs, FossilWatchAdapter adapter) {
        super(FileHandle.NOTIFICATION_FILTER, createFile(configs.toArray(new NotificationHRConfiguration[0])), adapter);
    }

    private static byte[] createFile(NotificationHRConfiguration[] configs) {
        int payloadLength = configs.length * 28;
        ByteBuffer buffer = ByteBuffer.allocate(payloadLength);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        for (NotificationHRConfiguration config : configs) {
            payloadLength = 26;

            buffer.putShort((short) payloadLength); //packet length

            byte[] crcBytes = config.getPackageCrc();

            // 6 bytes
            buffer.put(PacketID.PACKAGE_NAME_CRC.id)
                    .put((byte) 4)
                    .put(crcBytes);

            // 3 bytes
            buffer.put(PacketID.GROUP_ID.id)
                    .put((byte) 1)
                    .put((byte) 0);

            // 3 bytes
            buffer.put(PacketID.PRIORITY.id)
                    .put((byte) 1)
                    .put((byte) 0xFF);

            // 14 bytes
            buffer.put(PacketID.ICON.id)
                    .put((byte) 0x0C)
                    .put((byte) 0xFF)
                    .put((byte) 0x00)
                    .put((byte) 0x09)
                    .put(StringUtils.bytesToHex(crcBytes).getBytes())
                    .put((byte) 0x00);

        }

        return buffer.array();
    }

    enum PacketID {
        PACKAGE_NAME((byte) 0x01),
        SENDER_NAME((byte) 0x02),
        PACKAGE_NAME_CRC((byte) 0x04),
        GROUP_ID((byte) 0x80),
        APP_DISPLAY_NAME((byte) 0x81),
        ICON((byte) 0x82),
        PRIORITY((byte) 0xC1),
        MOVEMENT((byte) 0xC2),
        VIBRATION((byte) 0xC3);

        byte id;

        PacketID(byte id) {
            this.id = id;
        }
    }

    enum VibrationType {
        SINGLE_SHORT((byte) 5),
        DOUBLE_SHORT((byte) 6),
        TRIPLE_SHORT((byte) 7),
        SINGLE_LONG((byte) 8),
        SILENT((byte) 9);

        byte id;

        VibrationType(byte id) {
            this.id = id;
        }
    }
}

/*  Copyright (C) 2019-2021 Daniel Dakhno

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
package unikom.gery.damang.service.devices.qhybrid.requests.fossil.file;

import android.bluetooth.BluetoothGattCharacteristic;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import java.util.zip.CRC32;

import unikom.gery.damang.service.devices.qhybrid.adapter.fossil.FossilWatchAdapter;
import unikom.gery.damang.service.devices.qhybrid.file.FileHandle;
import unikom.gery.damang.service.devices.qhybrid.requests.fossil.FossilRequest;
import unikom.gery.damang.service.devices.qhybrid.requests.fossil_hr.file.ResultCode;

public abstract class FileGetRawRequest extends FossilRequest {
    private short handle;
    private FossilWatchAdapter adapter;

    private ByteBuffer fileBuffer;

    private byte[] fileData;

    private boolean finished = false;

    public FileGetRawRequest(short handle, FossilWatchAdapter adapter) {
        this.handle = handle;
        this.adapter = adapter;

        this.data =
                createBuffer()
                        .putShort(handle)
                        .putInt(0)
                        .putInt(0xFFFFFFFF)
                        .array();
    }

    public FileGetRawRequest(FileHandle handle, FossilWatchAdapter adapter){
        this(handle.getHandle(), adapter);
    }

    public FossilWatchAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean isFinished(){
        return finished;
    }

    @Override
    public void handleResponse(BluetoothGattCharacteristic characteristic) {
        byte[] value = characteristic.getValue();
        byte first = value[0];
        if(characteristic.getUuid().toString().equals("3dda0003-957f-7d4a-34a6-74696673696d")){
            if((first & 0x0F) == 1){
                ByteBuffer buffer = ByteBuffer.wrap(value);
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                short handle = buffer.getShort(1);
                int size = buffer.getInt(4);

                byte status = buffer.get(3);

                ResultCode code = ResultCode.fromCode(status);
                if(!code.inidicatesSuccess()){
                    throw new RuntimeException("FileGet error: " + code + "   (" + status + ")");
                }

                if(this.handle != handle){
                    throw new RuntimeException("handle: " + handle + "   expected: " + this.handle);
                }
                log("file size: " + size);
                fileBuffer = ByteBuffer.allocate(size);
            }else if((first & 0x0F) == 8){
                this.finished = true;

                ByteBuffer buffer = ByteBuffer.wrap(value);
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                short handle = buffer.getShort(1);
                if(this.handle != handle){
                    throw new RuntimeException("handle: " + handle + "   expected: " + this.handle);
                }

                CRC32 crc = new CRC32();
                crc.update(this.fileData);

                int crcExpected = buffer.getInt(8);

                if((int) crc.getValue() != crcExpected){
                    throw new RuntimeException("handle: " + handle + "   expected: " + this.handle);
                }

                this.handleFileRawData(this.fileData);
            }
        }else if(characteristic.getUuid().toString().equals("3dda0004-957f-7d4a-34a6-74696673696d")){
            fileBuffer.put(value, 1, value.length - 1);
            if((first & 0x80) == 0x80){
                this.fileData = fileBuffer.array();
            }
        }
    }

    @Override
    public UUID getRequestUUID() {
        return UUID.fromString("3dda0003-957f-7d4a-34a6-74696673696d");
    }

    @Override
    public byte[] getStartSequence() {
        return new byte[]{1};
    }

    @Override
    public int getPayloadLength() {
        return 11;
    }

    abstract public void handleFileRawData(byte[] fileData);
}

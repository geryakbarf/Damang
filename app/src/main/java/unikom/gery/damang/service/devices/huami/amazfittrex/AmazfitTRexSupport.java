/*  Copyright (C) 2017-2021 Andreas Shimokawa, Carsten Pfeiffer, Dmytro Bielik

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
package unikom.gery.damang.service.devices.huami.amazfittrex;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;

import unikom.gery.damang.R;
import unikom.gery.damang.devices.huami.HuamiFWHelper;
import unikom.gery.damang.devices.huami.amazfittrex.AmazfitTRexFWHelper;
import unikom.gery.damang.service.btle.TransactionBuilder;
import unikom.gery.damang.service.devices.huami.amazfitgts.AmazfitGTSSupport;

public class AmazfitTRexSupport extends AmazfitGTSSupport {

    @Override
    public HuamiFWHelper createFWHelper(Uri uri, Context context) throws IOException {
        return new AmazfitTRexFWHelper(uri, context);
    }

    @Override
    protected AmazfitTRexSupport setDisplayItems(TransactionBuilder builder) {
        setDisplayItemsNew(builder, false, true, R.array.pref_trex_display_items_default);
        return this;
    }

}

package app.aaps.plugins.sync.nsclientV3.extensions

import app.aaps.core.data.model.BS
import app.aaps.core.data.model.ICfg
import app.aaps.core.data.model.IDs
import app.aaps.core.data.pump.defs.PumpType
import app.aaps.core.nssdk.localmodel.treatment.NSBolus
import app.aaps.core.nssdk.mapper.convertToRemoteAndBack
import app.aaps.plugins.sync.extensions.contentEqualsTo
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class BolusExtensionKtTest {

    val iCfg = ICfg(insulinLabel = "Fake", insulinEndTime = 9 * 3600 * 1000, insulinPeakTime = 60 * 60 * 1000, concentration = 1.0)
    @Test
    fun toBolus() {
        var bolus = BS(
            timestamp = 10000,
            isValid = true,
            amount = 1.0,
            type = BS.Type.SMB,
            notes = "aaaa",
            isBasalInsulin = false,
            ids = IDs(
                nightscoutId = "nightscoutId",
                pumpId = 11000,
                pumpType = PumpType.DANA_I,
                pumpSerial = "bbbb"
            ),
            icfg = iCfg
        )

        var bolus2 = (bolus.toNSBolus().convertToRemoteAndBack() as NSBolus).toBolus()
        assertThat(bolus.contentEqualsTo(bolus2)).isTrue()
        assertThat(bolus.ids.contentEqualsTo(bolus2.ids)).isTrue()

        bolus = BS(
            timestamp = 10000,
            isValid = false,
            amount = 1.0,
            type = BS.Type.NORMAL,
            notes = "aaaa",
            isBasalInsulin = true,
            ids = IDs(
                nightscoutId = "nightscoutId",
                pumpId = 11000,
                pumpType = PumpType.DANA_I,
                pumpSerial = "bbbb"
            ),
            icfg = iCfg
        )

        bolus2 = (bolus.toNSBolus().convertToRemoteAndBack() as NSBolus).toBolus()
        assertThat(bolus.contentEqualsTo(bolus2)).isTrue()
        assertThat(bolus.ids.contentEqualsTo(bolus2.ids)).isTrue()
    }
}

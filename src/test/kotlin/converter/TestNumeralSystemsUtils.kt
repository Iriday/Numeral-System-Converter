package converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.lang.IllegalArgumentException
import java.math.BigInteger

class TestNumeralSystemsUtils {
    val illegalArgExcep = IllegalArgumentException().javaClass

    val one = 1
    val two = 2
    val four = 4
    val six = 6
    val eight = 8
    val ten = 10
    val sixteen = 16
    val twentyOne = 21
    val twentySeven = 27
    val thirtyThree = 33
    val thirtySix = 36

    @Test
    fun testDecimalTo() {
        // test integer
        fun testRange(range: LongRange, step: Long, base: Int) {
            for (i in range step step) {
                assertEquals(decimalTo(i.toString(), base), i.toString(base))
            }
        }

        fun testBaseOne(range: IntRange, step: Int) {
            for (i in range step step)
                assertEquals(decimalTo(i.toString(), one), "1".repeat(i))
        }

        // base 1
        testBaseOne(0..9999, 1)

        // base 2
        assertEquals(decimalTo("0", two), "0")
        assertEquals(decimalTo("7", two), "111")
        assertEquals(decimalTo("17", two), "10001")
        assertEquals(decimalTo("9999999", two), 9999999L.toString(two))
        assertEquals(decimalTo("999999119", two), 999999119L.toString(two))
        assertEquals(decimalTo("12349999999", two), 12349999999L.toString(two))
        assertEquals(decimalTo("1234999999912349999999", two), bi("1234999999912349999999").toString(two))
        testRange(0..10000L, 9, two)

        // base 6
        assertEquals(decimalTo("0", six), "0")
        assertEquals(decimalTo("5", six), "5")
        assertEquals(decimalTo("6", six), "10")
        assertEquals(decimalTo("17", six), 17L.toString(six))
        assertEquals(decimalTo("99929999", six), 99929999L.toString(six))
        assertEquals(decimalTo("19999119", six), 19999119L.toString(six))
        assertEquals(decimalTo("123499999299", six), 123499999299L.toString(six))
        assertEquals(decimalTo("3123499999991234999919", six), bi("3123499999991234999919").toString(six))
        testRange(0..10000L, 6, six)

        // base 8
        assertEquals(decimalTo("0", eight), "0")
        assertEquals(decimalTo("7", eight), "7")
        assertEquals(decimalTo("8", eight), "10")
        assertEquals(decimalTo("63", eight), "77")
        assertEquals(decimalTo("64", eight), "100")
        assertEquals(decimalTo("65", eight), "101")
        assertEquals(decimalTo("1234999992996", eight), 1234999992996L.toString(eight))
        assertEquals(decimalTo("312349999999123499991933", eight), bi("312349999999123499991933").toString(eight))
        testRange(0..99999L, 1, eight)

        // base 10
        testRange(0..9999L, 1, ten)
        testRange(99..99923478923947899L, 1149782343298, ten)
        assertEquals(
            decimalTo(
                "971234907812347890123490781234789012349087134789012349087123490797123490781234789012349078" +
                        "12347890123490871234907971234907812347890123490781234789012349087123490797123490781234789" +
                        "01234907812347890123490871234907971234907812347890123490781234789012349087123490797123490" +
                        "78123478901234907812347890123490871234907971234907812347890123490781234789012349087123490" +
                        "79712349078123478901234907812347890123490871234907971234907812347890123490781234789012349" +
                        "08712349079712349078123478901234907812347890123490871234907971234907812347890123490781234" +
                        "78901234908712349079712349078123478901234907812347890123490871234907971234907812347890123" +
                        "49078123478901234908712349079712349078123478901234907812347890123490871234907971234907812" +
                        "34789012349078123478901234908712349079712349078123478901234907812347890123490871234907971" +
                        "23490781234789012349078123478901234908712349079712349078123478901234907812347890123490871" +
                        "23490797123490781234789012349078123478901234908712349079712349971234907812347890123490781" +
                        "2347890123490871234900123499079712349078123478901234907812347890123490871234907", ten
            ),
            "971234907812347890123490781234789012349087134789012349087123490797123490781234789012349078" +
                    "12347890123490871234907971234907812347890123490781234789012349087123490797123490781234789" +
                    "01234907812347890123490871234907971234907812347890123490781234789012349087123490797123490" +
                    "78123478901234907812347890123490871234907971234907812347890123490781234789012349087123490" +
                    "79712349078123478901234907812347890123490871234907971234907812347890123490781234789012349" +
                    "08712349079712349078123478901234907812347890123490871234907971234907812347890123490781234" +
                    "78901234908712349079712349078123478901234907812347890123490871234907971234907812347890123" +
                    "49078123478901234908712349079712349078123478901234907812347890123490871234907971234907812" +
                    "34789012349078123478901234908712349079712349078123478901234907812347890123490871234907971" +
                    "23490781234789012349078123478901234908712349079712349078123478901234907812347890123490871" +
                    "23490797123490781234789012349078123478901234908712349079712349971234907812347890123490781" +
                    "2347890123490871234900123499079712349078123478901234907812347890123490871234907",
        )

        // base 16
        testRange(0..10999L, 1, sixteen)

        // base 21
        testRange(0..99991L, 1, twentyOne)

        // base 27
        testRange(0..123749L, 1, twentySeven)

        // base 36
        testRange(0..999999L, 3, thirtySix)

        // test fraction

        // base 1
        assertEquals(decimalTo("11.33333", one), "11111111111")

        // base 10
        assertEquals(decimalTo("0.3333399", ten, 10, fractionLen = 5), "0.33333") // no rounding!
        assertEquals(decimalTo("0.3333399", ten, fractionLen = 3), "0.333")
        assertEquals(decimalTo("999.3333399", ten, fractionLen = 7), "999.3333399")

        // tests exceptions
        assertThrows(illegalArgExcep) { decimalTo("", 11) }
        assertThrows(illegalArgExcep) { decimalTo("!@#", 11) }
        assertThrows(illegalArgExcep) { decimalTo("2..2", thirtySix) }
        assertThrows(illegalArgExcep) { decimalTo(".2.2", six) }
        assertThrows(illegalArgExcep) { decimalTo("2.2.", eight) }
        assertThrows(illegalArgExcep) { decimalTo("a", 11) }
        assertThrows(illegalArgExcep) { decimalTo("z", 11) }
        assertThrows(illegalArgExcep) { decimalTo("111a", ten) }
        assertThrows(illegalArgExcep) { decimalTo("1.a", thirtySix) }
    }


    @Test
    fun testToDecimal() {
        // test integer
        fun testRange(range: LongRange, step: Long, base: Int) {
            for (i in range step step) {
                assertEquals(toDecimal(i.toString(base), base), i.toString())
            }
        }

        fun testBaseOne(range: IntRange, step: Int) {
            for (i in range step step) {
                assertEquals(toDecimal("1".repeat(i), one), i.toString())
            }
        }

        // base 1
        testBaseOne(0..9999, 99)

        // base 2
        assertEquals(toDecimal("101", two), "5")
        testRange(0..999L, 1, two)

        // base 4
        testRange(0..999999L, 9, four)

        // base 8
        testRange(0..2798943L, 17, eight)

        // base 10
        testRange(0..10000L, 8, ten)
        assertEquals(
            toDecimal(
                "971234907812347890123490781234789012349087134789012349087123490797123490781234789012349078" +
                        "12347890123490871234907971234907812347890123490781234789012349087123490797123490781234789" +
                        "01234907812347890123490871234907971234907812347890123490781234789012349087123490797123490" +
                        "78123478901234907812347890123490871234907971234907812347890123490781234789012349087123490" +
                        "79712349078123478901234907812347890123490871234907971234907812347890123490781234789012349" +
                        "08712349079712349078123478901234907812347890123490871234907971234907812347890123490781234" +
                        "78901234908712349079712349078123478901234907812347890123490871234907971234907812347890123" +
                        "49078123478901234908712349079712349078123478901234907812347890123490871234907971234907812" +
                        "34789012349078123478901234908712349079712349078123478901234907812347890123490871234907971" +
                        "23490781234789012349078123478901234908712349079712349078123478901234907812347890123490871" +
                        "23490797123490781234789012349078123478901234908712349079712349971234907812347890123490781" +
                        "2347890123490871234900123499079712349078123478901234907812347890123490871234907",
                ten
            ), "971234907812347890123490781234789012349087134789012349087123490797123490781234789012349078" +
                    "12347890123490871234907971234907812347890123490781234789012349087123490797123490781234789" +
                    "01234907812347890123490871234907971234907812347890123490781234789012349087123490797123490" +
                    "78123478901234907812347890123490871234907971234907812347890123490781234789012349087123490" +
                    "79712349078123478901234907812347890123490871234907971234907812347890123490781234789012349" +
                    "08712349079712349078123478901234907812347890123490871234907971234907812347890123490781234" +
                    "78901234908712349079712349078123478901234907812347890123490871234907971234907812347890123" +
                    "49078123478901234908712349079712349078123478901234907812347890123490871234907971234907812" +
                    "34789012349078123478901234908712349079712349078123478901234907812347890123490871234907971" +
                    "23490781234789012349078123478901234908712349079712349078123478901234907812347890123490871" +
                    "23490797123490781234789012349078123478901234908712349079712349971234907812347890123490781" +
                    "2347890123490871234900123499079712349078123478901234907812347890123490871234907"
        )

        // base 16
        testRange(0..923479234234L, 999999, sixteen)

        // base 33
        testRange(0..234978923L, 189382, thirtyThree)

        // base 36
        testRange(0..87342L, 1, thirtySix)

        // test fraction

        // base 10
        assertEquals(toDecimal("392.999349", ten, scale = 6), "392.999349")

        // test exceptions
        assertThrows(illegalArgExcep) { toDecimal("", six) }
        assertThrows(illegalArgExcep) { toDecimal("!@#", ten) }
        assertThrows(illegalArgExcep) { toDecimal("2..2", thirtySix) }
        assertThrows(illegalArgExcep) { toDecimal(".2.2", six) }
        assertThrows(illegalArgExcep) { toDecimal("2.2.", eight) }
        assertThrows(illegalArgExcep) { toDecimal("1", 0) }
        assertThrows(illegalArgExcep) { toDecimal("1", 37) }
        assertThrows(illegalArgExcep) { toDecimal("1", -99) }
        assertThrows(illegalArgExcep) { toDecimal("1", 99) }
        assertThrows(illegalArgExcep) { toDecimal("b", 11) }
        assertThrows(illegalArgExcep) { toDecimal("f", 15) }
        assertThrows(illegalArgExcep) { toDecimal("z", 35) }
    }


    fun bi(num: Long): BigInteger {
        return num.toBigInteger()
    }

    fun bi(num: String): BigInteger {
        return num.toBigInteger()
    }
}

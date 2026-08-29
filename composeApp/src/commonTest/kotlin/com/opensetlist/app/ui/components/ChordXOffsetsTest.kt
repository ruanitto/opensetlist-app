package com.opensetlist.app.ui.components

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChordXOffsetsTest {

    private fun widths(chords: List<Pair<Int, String>>) =
        { chord: String -> (chord.length + 1).toFloat() }

    @Test
    fun chordsOnSameColumnDoNotOverlap() {
        val chords = listOf(
            0 to "D4",
            0 to "D/F#",
            0 to "G7"
        )
        val xs = chordXOffsets(chords, advancePx = 10f, gapPx = 2f, chordWidthPx = widths(chords))
        assertTrue(xs[1] >= xs[0] + widths(chords)("D4") + 2f)
        assertTrue(xs[2] >= xs[1] + widths(chords)("D/F#") + 2f)
    }

    @Test
    fun overlappingWideChordPushesNextOne() {
        val chords = listOf(
            0 to "D/F#",   // largura 5
            1 to "Dm7"     // coluna 1x10 = 10, mas o anterior termina ~ 0+5 = 5 < 10
        )
        val xs = chordXOffsets(chords, advancePx = 10f, gapPx = 2f, chordWidthPx = widths(chords))
        assertEquals(0f, xs[0])
        assertTrue(xs[1] >= xs[0] + widths(chords)("D/F#") + 2f)
    }

    @Test
    fun distantChordsStayAtNaturalColumn() {
        val chords = listOf(
            0 to "G",
            5 to "Em7"
        )
        val xs = chordXOffsets(chords, advancePx = 10f, gapPx = 2f, chordWidthPx = widths(chords))
        assertEquals(0f, xs[0])
        assertEquals(50f, xs[1])
    }

    @Test
    fun singleChordKeepsNaturalColumn() {
        val xs = chordXOffsets(listOf(3 to "C#m"), advancePx = 8f, gapPx = 2f, chordWidthPx = { 4f })
        assertEquals(24f, xs.single())
    }
}
package com.github.braillesystems.learnbraille.res

import com.github.braillesystems.learnbraille.data.dsl.materials
import com.github.braillesystems.learnbraille.data.dsl.symbols
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.data.entities.BrailleDots


val content by materials {
    +ruSymbols
    +specialSymbols
    +uebDigits
}

val practiceContent by materials {
    +ruSymbols
}

val ruSymbols by symbols(SymbolType.ru) {
    symbol(symbol = 'А', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(symbol = 'Б', brailleDots = BrailleDots(F, F, E, E, E, E))
    symbol(symbol = 'В', brailleDots = BrailleDots(E, F, E, F, F, F))
    symbol(symbol = 'Г', brailleDots = BrailleDots(F, F, E, F, F, E))
    symbol(symbol = 'Д', brailleDots = BrailleDots(F, E, E, F, F, E))
    symbol(symbol = 'Е', brailleDots = BrailleDots(F, E, E, E, F, E))
    symbol(symbol = 'Ё', brailleDots = BrailleDots(F, E, E, E, E, F))
    symbol(symbol = 'Ж', brailleDots = BrailleDots(E, F, E, F, F, E))
    symbol(symbol = 'З', brailleDots = BrailleDots(F, E, F, E, F, F))
    symbol(symbol = 'И', brailleDots = BrailleDots(E, F, E, F, E, E))
    symbol(symbol = 'Й', brailleDots = BrailleDots(F, F, F, F, E, F))
    symbol(symbol = 'К', brailleDots = BrailleDots(F, E, F, E, E, E))
    symbol(symbol = 'Л', brailleDots = BrailleDots(F, F, F, E, E, E))
    symbol(symbol = 'М', brailleDots = BrailleDots(F, E, F, F, E, E))
    symbol(symbol = 'Н', brailleDots = BrailleDots(F, E, F, F, F, E))
    symbol(symbol = 'О', brailleDots = BrailleDots(F, E, F, E, F, E))
    symbol(symbol = 'П', brailleDots = BrailleDots(F, F, F, F, E, E))
    symbol(symbol = 'Р', brailleDots = BrailleDots(F, F, F, E, F, E))
    symbol(symbol = 'С', brailleDots = BrailleDots(E, F, F, F, E, E))
    symbol(symbol = 'Т', brailleDots = BrailleDots(E, F, F, F, F, E))
    symbol(symbol = 'У', brailleDots = BrailleDots(F, E, F, E, E, F))
    symbol(symbol = 'Ф', brailleDots = BrailleDots(F, F, E, F, E, E))
    symbol(symbol = 'Х', brailleDots = BrailleDots(F, F, E, E, F, E))
    symbol(symbol = 'Ц', brailleDots = BrailleDots(F, E, E, F, E, E))
    symbol(symbol = 'Ч', brailleDots = BrailleDots(F, F, F, F, F, E))
    symbol(symbol = 'Ш', brailleDots = BrailleDots(F, E, E, E, F, F))
    symbol(symbol = 'Щ', brailleDots = BrailleDots(F, E, F, F, E, F))
    symbol(symbol = 'Ъ', brailleDots = BrailleDots(F, F, F, E, F, F))
    symbol(symbol = 'Ы', brailleDots = BrailleDots(E, F, F, F, E, F))
    symbol(symbol = 'Ь', brailleDots = BrailleDots(E, F, F, F, F, F))
    symbol(symbol = 'Э', brailleDots = BrailleDots(E, F, E, F, E, F))
    symbol(symbol = 'Ю', brailleDots = BrailleDots(F, F, E, E, F, F))
    symbol(symbol = 'Я', brailleDots = BrailleDots(F, F, E, F, E, F))
}

val specialSymbols by symbols(SymbolType.special){
    symbol(symbol = ']', brailleDots = BrailleDots(E, E, F, F, F, F)) // цифровой знак // number
    symbol(symbol = ',', brailleDots = BrailleDots(E, F, E, E, E, E)) // Знак препинания 'Запятая'
}

val uebDigits by symbols(SymbolType.digit){
    symbol(symbol = '1', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(symbol = '2', brailleDots = BrailleDots(F, F, E, E, E, E))
    symbol(symbol = '3', brailleDots = BrailleDots(F, E, E, F, E, E))
    symbol(symbol = '4', brailleDots = BrailleDots(F, E, E, F, F, E))
    symbol(symbol = '5', brailleDots = BrailleDots(F, E, E, E, F, E))
    symbol(symbol = '6', brailleDots = BrailleDots(F, F, E, F, E, E))
    symbol(symbol = '7', brailleDots = BrailleDots(F, F, E, F, F, E))
    symbol(symbol = '8', brailleDots = BrailleDots(F, F, E, E, F, E))
    symbol(symbol = '9', brailleDots = BrailleDots(E, F, E, F, E, E))
    symbol(symbol = '0', brailleDots = BrailleDots(E, F, E, F, F, E))
}



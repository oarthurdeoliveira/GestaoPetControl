package com.example.gestaopetcontrol.banco

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context): SQLiteOpenHelper(context, "banco.db", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        var SQL = """
            CREATE TABLE CLIENTES(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                NOME TEXT,
                CPF TEXT,
                TELEFONE TEXT,
                ENDERECO TEXT,
                NUMERO_RESIDENCIA INTEGER,
                COMPLEMENTO TEXT,
                REFERENCIA TEXT,
                CIDADE TEXT,
                BAIRRO TEXT,
                ESTADO TEXT,
                APAGADO INTEGER DEFAULT 0
                
            
            );
            
            
        """.trimIndent()
        p0?.execSQL(SQL)
    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
        TODO("Not yet implemented")
    }
}
package com.example.gestaopetcontrol.banco

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context): SQLiteOpenHelper(context, "banco.db", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        var sql_clientes = """
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
        var sql_pets = """
            CREATE TABLE PETS(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                NOME TEXT,
                ESPECIE TEXT,
                RACA TEXT,
                IDADE INTEGER,
                PESO INTEGER,
                ALERGIAS TEXT,
                OBSERVACOES TEXT,
                APAGADO INTEGER DEFAULT 0,
                ID_CLIENTE INTEGER,
                FOREIGN KEY (ID_CLIENTE) REFERENCES CLIENTES(ID)
            );
        """.trimIndent()

        var sql_agendamentos = """
            CREATE TABLE AGENDAMENTOS(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                DATA TEXT,
                HORA TEXT,
                OBSERVACOES TEXT,
                APAGADO INTEGER DEFAULT 0,
                ID_SERVICO INTEGER,
                ID_PET INTEGER,
                FOREIGN KEY (ID_SERVICO) REFERENCES SERVICOS(ID),
                FOREIGN KEY (ID_PET) REFERENCES PETS(ID)            
            );

            
            
        """.trimIndent()

        val sql_servicos = """
            CREATE TABLE SERVICOS(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                NOME TEXT,
                PRECO REAL,
                DESCRICAO TEXT,
                APAGADO INTEGER DEFAULT 0
            );
        """.trimIndent()


        p0?.execSQL(sql_clientes)
        p0?.execSQL(sql_pets)
        p0?.execSQL(sql_servicos)
        p0?.execSQL(sql_agendamentos)
        //inserirServicosPadrao(p0)

    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        db ?: return
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS SERVICOS(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                NOME TEXT,
                PRECO REAL,
                DESCRICAO TEXT,
                APAGADO INTEGER DEFAULT 0
            );
        """.trimIndent())
        adicionarColunaSeNaoExistir(db, "AGENDAMENTOS", "ID_SERVICO", "INTEGER")
        inserirServicosPadrao(db)
    }

    //TODO: REMOVER ISSO O DROP ANTES DA VERSÃO 1.0.0

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
        // So adicionar coisa na pós 1.0.0
    }

    private fun adicionarColunaSeNaoExistir(
        db: SQLiteDatabase,
        tabela: String,
        coluna: String,
        tipo: String
    ) {
        val cursor = db.rawQuery("PRAGMA table_info($tabela)", null)
        var existe = false
        while (cursor.moveToNext()) {
            if (cursor.getString(1) == coluna) {
                existe = true
                break
            }
        }
        cursor.close()
        if (!existe) {
            db.execSQL("ALTER TABLE $tabela ADD COLUMN $coluna $tipo")
        }
    }

    private fun inserirServicosPadrao(db: SQLiteDatabase?) {
        db ?: return
        val cursor = db.rawQuery("SELECT COUNT(*) FROM SERVICOS", null)
        cursor.moveToFirst()
        val total = cursor.getInt(0)
        cursor.close()

        if (total == 0) {
            val servicos = listOf(
                Triple("Banho", 50.0, "Servico padrao"),
                Triple("Tosa", 40.0, "Servico padrao"),
                Triple("Banho e Tosa", 80.0, "Servico padrao"),
                Triple("Consulta", 100.0, "Servico padrao"),
                Triple("Corte de unha", 20.0, "Servico padrao")
            )
            servicos.forEach { (nome, preco, descricao) ->
                db.execSQL(
                    "INSERT INTO SERVICOS (NOME, PRECO, DESCRICAO, APAGADO) VALUES (?, ?, ?, 0)",
                    arrayOf<Any>(nome, preco, descricao)
                )
            }
        }
    }
}

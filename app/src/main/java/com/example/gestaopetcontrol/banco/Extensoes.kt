package com.example.gestaopetcontrol.banco

import android.R
import android.annotation.SuppressLint
import android.content.ContentValues
import android.widget.Toast
import com.example.gestaopetcontrol.Clientes
import com.example.gestaopetcontrol.ClientesData

fun Database.inserirCliente(item: ClientesData): Long {
    val idcliente = writableDatabase.insert("CLIENTES", null, ContentValues().apply {
        put("NOME", item.nome)
        put("CPF", item.cpf)
        put("TELEFONE", item.telefone)
        put("ENDERECO", item.endereço)
        put("NUMERO_RESIDENCIA", item.numero_residencia)
        put("COMPLEMENTO", item.complemento)
        put("REFERENCIA", item.referencia)
        put("CIDADE", item.cidade)
        put("BAIRRO", item.bairro)
        put("ESTADO", item.estado)
        put("APAGADO", item.apagado)
    })
    return idcliente
}


@SuppressLint("Range")
fun Database.selecionarClientes(sql: String): List<ClientesData> {
    //val sql = "SELECT * FROM CLIENTES WHERE APAGADO = 0 ORDER BY NOME"
    //val sql = "SELECT * FROM CLIENTES WHERE CPF = '123.123.123-23' ORDER BY NOME"
    val cursor = readableDatabase.rawQuery(sql, null)
    val returnList = mutableListOf<ClientesData>()
    if (cursor.count > 0){
        while (cursor.moveToNext()){
            val cliente = ClientesData(
                nome = cursor.getString(cursor.getColumnIndex("NOME")),
                cpf = cursor.getString(cursor.getColumnIndex("CPF")),
                telefone = cursor.getString(cursor.getColumnIndex("TELEFONE")),
                endereço = cursor.getString(cursor.getColumnIndex("ENDERECO")),
                numero_residencia = cursor.getInt(cursor.getColumnIndex("NUMERO_RESIDENCIA")),
                complemento = cursor.getString(cursor.getColumnIndex("COMPLEMENTO")),
                referencia = cursor.getString(cursor.getColumnIndex("REFERENCIA")),
                cidade = cursor.getString(cursor.getColumnIndex("CIDADE")),
                bairro = cursor.getString(cursor.getColumnIndex("BAIRRO")),
                estado = cursor.getString(cursor.getColumnIndex("ESTADO")),
                apagado = cursor.getInt(cursor.getColumnIndex("APAGADO")),
                id = cursor.getInt(cursor.getColumnIndex("ID"))

            )
            returnList.add(cliente)
        }
        cursor.close()
    }
    return returnList
}


@SuppressLint("Range")
fun Database.pegaCliente(idCliente: Int?): ClientesData? {
    val sql = "SELECT * FROM CLIENTES WHERE ID = '${idCliente}'"
    val cursor = readableDatabase.rawQuery(sql, null)
    var clienteEncontrado: ClientesData? = null

    if (cursor.moveToFirst()){
        val nome = cursor.getString(cursor.getColumnIndex("NOME"))
        val cpf = cursor.getString(cursor.getColumnIndex("CPF"))
        val telefone = cursor.getString(cursor.getColumnIndex("TELEFONE"))
        val endereço = cursor.getString(cursor.getColumnIndex("ENDERECO"))
        val numero_residencia = cursor.getInt(cursor.getColumnIndex("NUMERO_RESIDENCIA"))
        val complemento = cursor.getString(cursor.getColumnIndex("COMPLEMENTO"))
        val referencia = cursor.getString(cursor.getColumnIndex("REFERENCIA"))
        val cidade = cursor.getString(cursor.getColumnIndex("CIDADE"))
        val bairro = cursor.getString(cursor.getColumnIndex("BAIRRO"))
        val estado = cursor.getString(cursor.getColumnIndex("ESTADO"))
        val apagado = cursor.getInt(cursor.getColumnIndex("APAGADO"))
        val id = cursor.getInt(cursor.getColumnIndex("ID"))

        clienteEncontrado = ClientesData(nome, cpf, telefone, endereço, numero_residencia, complemento, referencia, cidade, bairro, estado, apagado, id)
    }
    cursor.close()



    return clienteEncontrado
}

fun Database.atualizarCliente(item: ClientesData): Int {
    val valores = ContentValues().apply {
        put("NOME", item.nome)
        put("CPF", item.cpf)
        put("TELEFONE", item.telefone)
        put("ENDERECO", item.endereço)
        put("NUMERO_RESIDENCIA", item.numero_residencia)
        put("COMPLEMENTO", item.complemento)
        put("REFERENCIA", item.referencia)
        put("CIDADE", item.cidade)
        put("BAIRRO", item.bairro)
        put("ESTADO", item.estado)
        put("APAGADO", item.apagado)
    }
    return writableDatabase.update("CLIENTES", valores, "ID=${item.id}", null)
}

fun Database.apagarCliente(idClientes: Int?): Int {
    val valores = ContentValues().apply {
        put("APAGADO", 1)
    }

    return writableDatabase.update("CLIENTES", valores, "ID=${idClientes}", null)
}
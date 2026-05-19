package com.example.gestaopetcontrol.banco

import android.annotation.SuppressLint
import android.content.ContentValues
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
fun Database.selecionarClientes(): List<ClientesData> {
    val sql = "SELECT * FROM CLIENTES WHERE APAGADO = 0 ORDER BY NOME"
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
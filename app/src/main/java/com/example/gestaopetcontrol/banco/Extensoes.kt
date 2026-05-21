package com.example.gestaopetcontrol.banco

import android.R
import android.annotation.SuppressLint
import android.content.ContentValues
import android.widget.Toast
import com.example.gestaopetcontrol.AgendamentoData
import com.example.gestaopetcontrol.Clientes
import com.example.gestaopetcontrol.ClientesData
import com.example.gestaopetcontrol.PetData
import com.example.gestaopetcontrol.Pets



fun Database.inserirAgendamento(item: AgendamentoData): Long{
    val idAgendamento = writableDatabase.insert("AGENDAMENTOS", null, ContentValues().apply {
        put("DATA", item.data)
        put("HORA", item.hora)
        put("OBSERVACOES", item.observacao)
        put("APAGADO", item.apagado)
        put("ID_PET", item.idPet)
    })
    return idAgendamento
}

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

fun Database.inserirPet(item: PetData): Long {
    val idpet = writableDatabase.insert("PETS", null, ContentValues().apply {
        put("NOME", item.nome)
        put("ESPECIE", item.especie)
        put("RACA", item.raca)
        put("IDADE", item.idade)
        put("PESO", item.peso)
        put("ALERGIAS", item.alergias)
        put("OBSERVACOES", item.observacoes)
        put("ID_CLIENTE", item.idCliente)
    })
    return idpet
}

@SuppressLint("Range")
fun Database.selecionarAgendamentos(): List<AgendamentoData> {
    val sql = """
         SELECT AGENDAMENTOS.*, CLIENTES.NOME as NOME_DONO, CLIENTES.CPF as CPF_DONO, PETS.NOME as NOME_PET, PETS.RACA as RACA_PET, PETS.ESPECIE as ESPECIE_PET
         FROM AGENDAMENTOS
         INNER JOIN PETS ON AGENDAMENTOS.ID_PET = PETS.ID
         INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID
         WHERE (AGENDAMENTOS.DATA || ' ' || AGENDAMENTOS.HORA) >= datetime('now', 'localtime')
         ORDER BY (AGENDAMENTOS.DATA || ' ' || AGENDAMENTOS.HORA) ASC
    """.trimIndent()
    val cursor = readableDatabase.rawQuery(sql, null)
    val returnList = mutableListOf<AgendamentoData>()
    if (cursor.count > 0) {
        while(cursor.moveToNext()){
            val agendamento = AgendamentoData(
                data = cursor.getString(cursor.getColumnIndex("DATA")),
                hora = cursor.getString(cursor.getColumnIndex("HORA")),
                observacao = cursor.getString(cursor.getColumnIndex("OBSERVACOES")),
                apagado = cursor.getInt(cursor.getColumnIndex("APAGADO")),
                nome_pet = cursor.getString(cursor.getColumnIndex("NOME_PET")),
                nome_dono = cursor.getString(cursor.getColumnIndex("NOME_DONO")),
                cpf_dono = cursor.getString(cursor.getColumnIndex("CPF_DONO")),
                raca_pet = cursor.getString(cursor.getColumnIndex("RACA_PET")),
                especie_pet = cursor.getString(cursor.getColumnIndex("ESPECIE_PET")),
                id = cursor.getInt(cursor.getColumnIndex("ID")),
                idPet = cursor.getInt(cursor.getColumnIndex("ID_PET"))
            )
            returnList.add(agendamento)
        }
        cursor.close()

    }
    return returnList

}

@SuppressLint("Range")
fun Database.selecionarPets(sql: String): List<PetData> {
    val cursor = readableDatabase.rawQuery(sql, null)
    val returnList = mutableListOf<PetData>()
    if (cursor.count > 0){
        while (cursor.moveToNext()){
            val pet = PetData(
                nome = cursor.getString(cursor.getColumnIndex("NOME")),
                especie = cursor.getString(cursor.getColumnIndex("ESPECIE")),
                raca = cursor.getString(cursor.getColumnIndex("RACA")),
                idade = cursor.getInt(cursor.getColumnIndex("IDADE")),
                peso = cursor.getInt(cursor.getColumnIndex("PESO")),
                alergias = cursor.getString(cursor.getColumnIndex("ALERGIAS")),
                observacoes = cursor.getString(cursor.getColumnIndex("OBSERVACOES")),
                apagado = cursor.getInt(cursor.getColumnIndex("APAGADO")),
                idCliente = cursor.getInt(cursor.getColumnIndex("ID_CLIENTE")),
                nome_cliente = cursor.getString(cursor.getColumnIndex("CLIENTE_NOME")),
                cpf_cliente = cursor.getString(cursor.getColumnIndex("CLIENTE_CPF")),
                id = cursor.getInt(cursor.getColumnIndex("ID"))

            )
            returnList.add(pet)
        }
        cursor.close()
    }

    return returnList
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
fun Database.pegaClienteCPF(CPF: String): ClientesData? {
    val sql = "SELECT * FROM CLIENTES WHERE CPF = '${CPF}' AND APAGADO = 0"
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


@SuppressLint("Range")
fun Database.pegarPet(idPet: Int?): PetData? {
    val sql = """
        SELECT PETS.*, CLIENTES.NOME as CLIENTE_NOME, CLIENTES.CPF as CLIENTE_CPF 
        FROM PETS 
        INNER JOIN CLIENTES ON PETS.ID_CLIENTE = CLIENTES.ID 
        WHERE PETS.ID = '${idPet}'
    """.trimIndent()

    val cursor = readableDatabase.rawQuery(sql, null)
    var petsencontrados: PetData? = null

    if (cursor.moveToFirst()) {

        val nome = cursor.getString(cursor.getColumnIndex("NOME"))
        val especie = cursor.getString(cursor.getColumnIndex("ESPECIE"))
        val raca = cursor.getString(cursor.getColumnIndex("RACA"))
        val idade = cursor.getInt(cursor.getColumnIndex("IDADE"))
        val peso = cursor.getInt(cursor.getColumnIndex("PESO"))
        val alergias = cursor.getString(cursor.getColumnIndex("ALERGIAS"))
        val observacoes = cursor.getString(cursor.getColumnIndex("OBSERVACOES"))
        val apagado = cursor.getInt(cursor.getColumnIndex("APAGADO"))
        val idCliente = cursor.getInt(cursor.getColumnIndex("ID_CLIENTE"))
        val nome_cliente = cursor.getString(cursor.getColumnIndex("CLIENTE_NOME"))
        val cpf_cliente = cursor.getString(cursor.getColumnIndex("CLIENTE_CPF"))
        val id = cursor.getInt(cursor.getColumnIndex("ID"))

        petsencontrados = PetData(nome, especie, raca, idade, peso, alergias, observacoes, apagado, idCliente, cpf_cliente, nome_cliente, id)
    }
    cursor.close()

    return petsencontrados
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

fun Database.apagarPet(idPet: Int?): Int {
    val valores = ContentValues().apply {
        put("APAGADO", 1)
    }

    return writableDatabase.update("PETS", valores, "ID=${idPet}", null)
}

fun Database.atualizarPet(item: PetData): Int {
    val valores = ContentValues().apply {
        put("NOME", item.nome)
        put("ESPECIE", item.especie)
        put("RACA", item.raca)
        put("IDADE", item.idade)
        put("PESO", item.peso)
        put("ALERGIAS", item.alergias)
        put("OBSERVACOES", item.observacoes)
        put("APAGADO", item.apagado)
        put("ID_CLIENTE", item.idCliente)
    }
    return writableDatabase.update("PETS", valores, "ID=${item.id}", null)
}
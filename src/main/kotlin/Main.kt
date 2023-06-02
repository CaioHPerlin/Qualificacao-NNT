import java.io.File
import java.io.FileReader
import java.io.FileWriter

fun checkExistance(file: File){
    if(!file.exists() || file.length().toInt() == 0) {
        val writer = FileWriter(file)
        writer.write("Nome/Cargo/Salário\n")
        writer.close()
    }
}
fun main() {
    val file = File("funcionarios.csv")

    while (true) {
        checkExistance(file)
        println("Selecione a opção desejada:")
        println("1 - Cadastrar Funcionário")
        println("2 - Listar Funcionários")
        println("3 - Excluir Funcionário")
        println("4 - Limpar Registros")
        println("5 - Sair")

        val input = readLine()?.toIntOrNull() ?: continue

        when (input) {
            1 -> createEmployee(file)
            2 -> listEmployees(file)
            3 -> deleteEmployee(file)
            4 -> eraseFile(file)
            5 -> return
            else -> println("Opção inválida\n")
        }
    }
}

fun createEmployee(file: File) {
    val reader = FileReader(file)
    val lines = reader.readLines().toMutableList()
    reader.close()

    print("Nome do Funcionário:")
    val name = readLine() ?: return

    print("Cargo do Funcionário:")
    val role = readLine() ?: return

    print("Salário do Funcionário (R$):")
    val salary = readLine() ?: return

    if(name == "" || role == "" || salary == ""){
        println("Um ou mais campos vazios.\n")
        return createEmployee(file)
    }

    lines.add("$name/$role/$salary")

    val writer = FileWriter(file)

    writer.write(lines.joinToString("\n"))
    writer.close()

    println("Funcionário cadastrado com sucesso.\n")
}

fun listEmployees(file: File) {
    val reader = FileReader(file)
    val lines = reader.readLines()
    reader.close()

    if (lines.isEmpty() || lines.size == 1) {
        println("Não há registro de nenhum funcionário.\n")
        return
    }

    var widthN = 0
    var widthR = 0
    var widthS = 0
    for (line in lines) {
        val fields = line.split("/")
        val name = fields[0]
        val role = fields[1]
        val salary = fields[2]
        if(name.length > widthN) widthN = name.length
        if(role.length > widthR) widthR = role.length
        if(salary.length > widthS) widthS = salary.length
    }

    for ((i, line) in lines.withIndex()) {
        val fields = line.split("/")
        val name = fields[0]
        val role = fields[1]
        val salary = fields[2]

        val template = if(i == 0) salary else String.format("R$ %,d", salary.toInt())
        println(String.format("%-${widthN}s | %-${widthR}s | %-${widthS}s", name, role, template))
    }
}

fun deleteEmployee(file: File) {
    val reader = FileReader(file)
    val lines = reader.readLines().toMutableList()
    reader.close()

    if (lines.isEmpty() || lines.size == 1) {
        println("Não há registro de nenhum funcionário.\n")
        return
    }

    print("Insira o nome do funcionário que deseja excluir:")
    val employee = readLine() ?: return

    var index = -1
    for ((i, line) in lines.withIndex()) {
        val fields = line.split("/")
        if (fields[0].lowercase().replace(" ", "") == employee.lowercase().replace(" ", "")) {
            index = i
            break
        }
    }

    if (index == -1) {
        println("Funcionário não encontrado.\n")
        return
    }

    println("Tem certeza que deseja excluir o funcionário $employee?")
    println("1 - Sim (padrão)")
    println("2 - Cancelar")
    val confirm = readLine()?.toIntOrNull() ?: 1

    if(confirm != 1) {
        println("Operação cancelada.\n")
        return
    }

    lines.removeAt(index)

    val writer = FileWriter(file)
    writer.write(lines.joinToString("\n"))
    writer.close()

    println("Registro excluído com sucesso.\n")
}

fun eraseFile(file: File) {
    if (file.exists()) {
        file.delete()
        println("Registro excluído.\n")
    } else {
        println("Registro não existe.\n")
    }
}
package com.mycompany.cadastro_de_trecos.crud;

import static com.mycompany.cadastro_de_trecos.Cadastro_de_trecos.*;
import com.mycompany.cadastro_de_trecos.db.DbConnection;
import com.mycompany.cadastro_de_trecos.setup.AppSetup;
import java.sql.SQLException;
import java.util.Scanner;

public class Update extends AppSetup {

    public static void update() {

        // Inicializa e reserva recursos.
        int id = 0;
        String sql;

        // Cabeçalho da view.
        System.out.println(appName + "\n" + appSep);
        System.out.println("Editar registro");
        System.out.println(appSep);

        try {

            // Recebe o Id do teclado.
            System.out.print("Digite o ID ou [0] para retornar: ");
            id = Integer.parseInt(scanner.next());
            if (id == 0) {
                clearScreen();
                mainMenu();
            }
        } catch (NumberFormatException e) {

            // Quando opção é inválida.
            clearScreen();
            System.out.println("Oooops! Opção inválida!\n");
            update();
        }

        try {

            // Obtém o registro solicitado do banco de dados.
            sql = "SELECT * FROM " + DBTABLE + " WHERE id = ? AND status = '2'; ";
            conn = DbConnection.dbConnect();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            res = pstm.executeQuery();
            if (res.next()) {
                String sepDH[] = res.getString("data").split(" ");
                    String sepD[] = sepDH[0].split("-");
                    String novoDH = sepD[2] + "/" + sepD[1] + "/" + sepD[0] + " " + sepDH[1];
                    String nStatus = null;
                    if (res.getString("status").equals("1")) {
                        nStatus = "BLOQUEADO";
                    }
                    if (res.getString("status").equals("2")) {
                        nStatus = "ATIVO";
                    }
                    // Exibe registro na view.
                    System.out.println(
                            "\nID: " + res.getString("id") + "\n"
                            + "  Nome: " + res.getString("nome") + "\n"
                            + "  Descrição: " + res.getString("descricao") + "\n"
                            + "  Localização: " + res.getString("localizacao") + "\n"
                            + "  Status: " + nStatus + "\n"
                            + "  Data: " + novoDH + "\n"                
                );

                System.out.println("Insira os novos dados ou deixe em branco para manter os atuais:\n");

                // Instância de Scanner para obter dados do teclado.
                Scanner keyboard = new Scanner(System.in, "latin1");

                System.out.print("\tNome: ");
                String itemName = keyboard.nextLine().trim();

                System.out.print("\tDescrição: ");
                String itemDescription = keyboard.nextLine().trim();
                
                System.out.print("\tLocalização: ");
                String itemLocalization = keyboard.nextLine().trim();

                // Pede confirmação.
                System.out.print("\nOs dados acima estão corretos? [s/N] ");
                if (keyboard.next().trim().toLowerCase().equals("s")) {

                    // Short Hand → https://www.w3schools.com/java/java_conditions_shorthand.asp
                    String saveName = (itemName.equals("")) ? res.getString("nome") : itemName;
                    String saveDescription = (itemDescription.equals("")) ? res.getString("descricao") : itemDescription;                    
                    String saveLocalization = (itemLocalization.equals("")) ? res.getString("localizacao") : itemLocalization;

                    // Atualiza registro no banco de dados.
                    sql = "UPDATE " + DBTABLE + " SET nome = ?, descricao = ?, localizacao = ? WHERE id = ? AND status = '2';";
                    pstm = conn.prepareStatement(sql);
                    pstm.setString(1, saveName);
                    pstm.setString(2, saveDescription);                    
                    pstm.setString(3, saveLocalization);
                    pstm.setInt(4, id);
                    if (pstm.executeUpdate() == 1) {

                        // Se o registro foi criado.
                        System.out.println("\nRegistro atualizado!");
                    } else {

                        // Falha ao criar registro.
                        System.out.println("Nada aconteceu!");
                    }

                } else {
                    System.out.println("\nNada aconteceu!");
                }

                // Fecha banco de dados.
                DbConnection.dbClose(res, stmt, pstm, conn);

                // Menu inferior da seção.
                System.out.println(appSep);
                System.out.println("Menu:\n\t[1] Menu principal\n\t[2] Editar outro\n\t[0] Sair");
                System.out.println(appSep);

                // Recebe opção do teclado.            
                System.out.print("Opção: ");
                String option = scanner.next();

                // Executa conforme a opção.
                switch (option) {
                    case "0":
                        exitProgram();
                        break;
                    case "1":
                        clearScreen();
                        mainMenu();
                        break;
                    case "2":
                        clearScreen();
                        update();
                        break;
                    default:
                        clearScreen();
                        System.out.println("Oooops! Opção inválida!\n");
                        update();
                }

            } else {
                // Se não tem registro.
                clearScreen();
                System.out.println("Oooops! Não achei nada!\n");
                update();
            }

        } catch (SQLException e) {
        }

    }

}

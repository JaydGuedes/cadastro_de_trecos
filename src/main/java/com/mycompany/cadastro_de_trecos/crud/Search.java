package com.mycompany.cadastro_de_trecos.crud;

import static com.mycompany.cadastro_de_trecos.Cadastro_de_trecos.*;
import com.mycompany.cadastro_de_trecos.db.DbConnection;
import com.mycompany.cadastro_de_trecos.setup.AppSetup;
import java.sql.SQLException;

public class Search extends AppSetup{
    public static void search(){
         
        
        String coisa = null;
        String sql;
        // Cabeçalho da view.
        System.out.println(appName + "\n" + appSep);
        System.out.println("Buscar por palavra chave");
        System.out.println(appSep + "\n");        
              
        
        try {

            // Recebe o Id do teclado.
            System.out.print("Digite a sua busca: ");
            coisa = scanner.next().trim();
            if (coisa.equals("")) {
                clearScreen();
                mainMenu();
            }
        } catch (Exception e) {

            // Quando opção é inválida.
            clearScreen();
            System.out.println("Oooops! Opção inválida!\n");
            search();
        }
        
        try {

            // Verifica se o registro existe.
            sql = "SELECT * FROM " + DBTABLE + " WHERE name LIKE ? OR description LIKE ?";
            conn = DbConnection.dbConnect();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, "%" + coisa + "%");
            pstm.setString(2, "%" + coisa + "%");
            res = pstm.executeQuery();

            System.out.println(" ");
            if (res.next()) {

                // Se encontrou registros.
                do {

                    // Exibe registro na view.
                    System.out.println(
                            "ID: " + res.getString("id") + "\n"
                            + "  Nome: " + res.getString("name") + "\n"
                            + "  Descrição: " + res.getString("description") + "\n"
                    );
                } while (res.next());
            } else {

                // Se não encontrou registro.
                clearScreen();
                System.out.println("Oooops! Não achei nada!\n");
            }

            // Fecha banco de dados.
            DbConnection.dbClose(res, stmt, pstm, conn);

            // Menu inferior da seção.
            System.out.println(appSep);
            System.out.println("Menu:\n\t[1] Menu principal\n\t[2] Buscar outro\n\t[0] Sair");
            System.out.println(appSep);

            // Recebe opção do teclado.            
            System.out.print("Opção: ");
            String option = scanner.next();

            // Executa conforme a opção.
            switch (option) {
                case "0" ->
                    exitProgram();
                case "1" -> {
                    clearScreen();
                    mainMenu();
                }
                case "2" -> {
                    clearScreen();
                    search();
                }
                default -> {
                    clearScreen();
                    System.out.println("Oooops! Opção inválida!\n");
                    search();
                }
            }

        } catch (SQLException error) {

            // Tratamento de erros.
            System.out.println("Oooops! " + error.getMessage());
            System.exit(0);
        }

}
    
}

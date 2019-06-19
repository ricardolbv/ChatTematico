import java.net.*;
import java.util.*;

public class AceitadoraDeConexao extends Thread
{
    private ServerSocket             pedido;
    private HashMap<String,HashMap<String,Parceiro>> tematico;
	

    public AceitadoraDeConexao
    (int porta, HashMap<String,HashMap<String,Parceiro>> tematico)
    throws Exception
    {
        if (tematico==null)
            throw new Exception ("Usuarios ausentes");

        this.tematico = tematico;

        try
        {
            this.pedido =
            new ServerSocket (porta);
        }
        catch (Exception erro)
        {
            throw new Exception ("Porta invalida");
        }
    }

    public void run ()
    {
        for(;;)
        {
            Socket conexao=null;
            try
            {
                conexao = this.pedido.accept();
				
            }
            catch (Exception erro)
            {
                continue;
            }

            SupervisoraDeConexao supervisoraDeConexao=null;
            try
            {
                supervisoraDeConexao =
                new SupervisoraDeConexao (conexao, tematico);
            }
            catch (Exception erro)
            {} // sei que passei parametros corretos para o construtor
            supervisoraDeConexao.start();
        }
    }
}

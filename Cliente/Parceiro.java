import java.io.*;
import java.net.*;
import java.util.*;

public class Parceiro
{
    private Socket             conexao;
    private ObjectInputStream  receptor;
    private ObjectOutputStream transmissor;

    public Parceiro (Socket             conexao,
                     ObjectInputStream  receptor,
                     ObjectOutputStream transmissor)
                     throws Exception // se parametro nulos
    {
        if (conexao==null)
            throw new Exception ("Conexao ausente");

        if (receptor==null)
            throw new Exception ("Receptor ausente");

        if (transmissor==null)
            throw new Exception ("Transmissor ausente");

        this.conexao     = conexao;
        this.receptor    = receptor;
        this.transmissor = transmissor;
    }

    public void receba (Comunicado x) throws Exception
    {
		try
	    {
			this.transmissor.writeObject (x);
			this.transmissor.flush       ();
        }
        catch (IOException erro)
        {
			throw new Exception ("Erro de transmissao");
		}
    }

    public Comunicado envie () throws Exception
    {
        try
        {
            return (Comunicado)this.receptor.readObject();
        }
        catch (Exception erro)
        {
			throw new Exception ("Erro de recepcao");
        }
    }

    public void adeus () throws Exception
    {
		boolean deuErro=false;

        try
        {
            this.transmissor.close();
        }
        catch (Exception erro)
        {
            deuErro=true;
		}

        try
        {
            this.receptor.close();
        }
        catch (Exception erro)
        {
			deuErro=true;
		}

        try
        {
            this.conexao.close();
        }
        catch (Exception erro)
        {
            deuErro=true;
		}

		if (deuErro)
			throw new Exception ("Erro de desconexao");
    }
}
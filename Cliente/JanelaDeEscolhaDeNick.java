
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;




class JanelaDeEscolhaDeNick extends    JFrame
                            implements WindowListener,
                                       MouseListener,
                                       KeyEventDispatcher
{
    private static final long serialVersionUID = -1096994042272136989L;

    private JanelaDeChat janelaDeChat;

    private Parceiro servidor;
	private String temas;

    private JButton    btnOk   = new JButton    ("OK");
    private JTextField txfNick = new JTextField ();
	private JTextField txfTema = new JTextField ();  // Vou por o tema aqui,
	
	
	

    public JanelaDeEscolhaDeNick (Parceiro servidor, String temas)
    throws Exception
    {
        if (servidor==null)
            throw new Exception ("Servidor indisponivel");
		
		if (temas==null)
			throw new Exception ("Temas indisponiveis");

        this.servidor = servidor;
		this.temas = temas;
		
		
		
		
	//	Jlabel tema = new Jlabel ("Temas: ")
		

        this.setTitle ("Escolha de nick");
        this.setSize  (650, 200);

        Font fntPtFixa  = new Font ("Arial", Font.BOLD,  16);
        Font fntPtVar   = new Font ("Arial", Font.PLAIN, 13);

        this.btnOk  .setFont (fntPtFixa);
        this.txfNick.setFont (fntPtVar);
		this.txfTema.setFont (fntPtVar);

        JLabel lblNick = new JLabel ("Nick:");
        lblNick.setFont (fntPtFixa);
		
		JLabel lblTema = new JLabel ("Tema:");
        lblTema.setFont (fntPtFixa);
		
		JLabel lblTemas = new JLabel ("->"+temas);
		lblTemas.setFont (fntPtFixa);

        this.setLayout (new GridLayout (3,1));

        this.add (new JLabel ());
        this.add (lblNick);
        this.add (this.txfNick);
		this.add (new JLabel ());
		this.add (lblTema);
		this.add (this.txfTema);
        this.add (btnOk);
        this.add (new JLabel ());
		this.add(lblTemas);

        this .addWindowListener (this);
        btnOk.addMouseListener  (this);

        this.setLocationRelativeTo (null); // centraliza
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

        this.setVisible (true);
    }
		
    public JanelaDeChat getJanelaDeChat ()
    {
        while (this.janelaDeChat==null)
            Thread.currentThread().yield ();

        return this.janelaDeChat;
    }




    public void windowClosing (WindowEvent evt)
    {
        System.exit (0);
    }

    public void windowOpened (WindowEvent evt)
    {}

    public void windowClosed (WindowEvent evt)
    {}

    public void windowActivated (WindowEvent evt)
    {}

    public void windowDeactivated (WindowEvent evt)
    {}

    public void windowIconified (WindowEvent evt)
    {}

    public void windowDeiconified (WindowEvent evt)
    {}




    private void trateClickEmOk ()
    {
			String tema = "";			// verifico se o tema digitado esta entre os diponiveis!!!
			tema = this.txfTema.getText();
			tema.toLowerCase();
			
			
        if (!this.txfNick.getText().equals("") && !this.txfTema.getText().equals(""))
        {
            boolean nickCerto 		= true;
			boolean temaContido 	= true;

            if (this.txfNick.getText().toUpperCase().equals("TODOS"))
                nickCerto = false;
			else if (!this.temas.contains(tema))
				temaContido = false;
            else
            {
                try
                {
                    this.servidor.receba (
                    new Comunicado ("NIK", this.txfNick.getText (), this.txfTema.getText())); // TODO: Fazer enviar a escolha tambem do tema 

                    Comunicado comunicado = this.servidor.envie ();

                    if (comunicado.getComando().equals("ERR"))
                        nickCerto = false;
                }
                catch (Exception erro)
                {
                    JOptionPane.showMessageDialog(null/*sem janela m�e*/,
                    "Tente novamente mais tarde!",
                    "Erro de conectividade",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
			

            if (nickCerto && temaContido)
            {
                try
                {
                    DefaultKeyboardFocusManager.
                    getCurrentKeyboardFocusManager().
                    removeKeyEventDispatcher (this);

                    this.janelaDeChat =
                    new JanelaDeChat (
                                        this.txfNick.getText(),this.servidor,this.txfTema.getText());
                }
                catch (Exception erro)
                {} // sei que nem nick, nem servidor sao null
            }
            else // if (comunicado.getComando().equals("ERR"))
                JOptionPane.showMessageDialog(null/*sem janela m�e*/,
                "O nick escolhido � inv�lido ou o tema nao esta disponivel",
                "Escolha outro nick ou escolha um dos temas indicados na '->'",
                JOptionPane.ERROR_MESSAGE);
		}
        
    }

    public void mouseClicked (MouseEvent evt)
    {
        if (evt.getButton       () == MouseEvent.BUTTON1 &&
            evt.getClickCount   () == 1                  &&
           !evt.isAltDown       ()                       &&
           !evt.isAltGraphDown  ()                       &&
           !evt.isControlDown   ()                       &&
           !evt.isMetaDown      ()                       &&
           !evt.isShiftDown     ())
            this.trateClickEmOk ();
    }

    public void mousePressed (MouseEvent evt)
    {}

    public void mouseReleased (MouseEvent evt)
    {}

    public void mouseEntered (MouseEvent evt)
    {}

    public void mouseExited (MouseEvent evt)
    {}




    public boolean dispatchKeyEvent(KeyEvent evt)
    {
        if (evt.getID() == KeyEvent.KEY_PRESSED)
            if (evt.getKeyCode() == KeyEvent.VK_ENTER)
                this.trateClickEmOk();

        return false; // continua a acao normalmente
    }
}

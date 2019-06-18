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

    private JButton    btnOk   = new JButton    ("OK");
    private JTextField txfNick = new JTextField ();
	private JTextField txfTema = new JTextField ();  // Vou por o tema aqui,
	

    public JanelaDeEscolhaDeNick (Parceiro servidor)
    throws Exception
    {
        if (servidor==null)
            throw new Exception ("Servidor indisponivel");

        this.servidor = servidor;

        this.setTitle ("Escolha de nick");
        this.setSize  (350, 200);

        Font fntPtFixa  = new Font ("Arial", Font.BOLD,  16);
        Font fntPtVar   = new Font ("Arial", Font.PLAIN, 13);

        this.btnOk  .setFont (fntPtFixa);
        this.txfNick.setFont (fntPtVar);
		this.txfTema.setFont (fntPtVar);

        JLabel lblNick = new JLabel ("Nick:");
        lblNick.setFont (fntPtFixa);
		
		JLabel lblTema = new JLabel ("Tema:");
        lblTema.setFont (fntPtFixa);

        this.setLayout (new GridLayout (5,1));

        this.add (new JLabel ());
        this.add (lblNick);
        this.add (this.txfNick);
        this.add (btnOk);
        this.add (new JLabel ());
		this.add (new JLabel ());
		this.add (lblTema);
		this.add (this.txfTema);

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
        if (!this.txfNick.getText().equals(""))
        {
            boolean nickCerto = true;

            if (this.txfNick.getText().toUpperCase().equals("TODOS"))
                nickCerto = false;
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
                    JOptionPane.showMessageDialog(null/*sem janela mãe*/,
                    "Tente novamente mais tarde!",
                    "Erro de conectividade",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (nickCerto)
            {
                try
                {
                    DefaultKeyboardFocusManager.
                    getCurrentKeyboardFocusManager().
                    removeKeyEventDispatcher (this);

                    this.janelaDeChat =
                    new JanelaDeChat (
                                        this.txfNick.getText(),this.servidor);
                }
                catch (Exception erro)
                {} // sei que nem nick, nem servidor sao null
            }
            else // if (comunicado.getComando().equals("ERR"))
                JOptionPane.showMessageDialog(null/*sem janela mãe*/,
                "O nick escolhido é inválido ou já está em uso!",
                "Escolha outro nick",
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

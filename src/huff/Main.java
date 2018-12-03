package huff;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

@SuppressWarnings("ALL")
public class Main extends JFrame {
    public Main() {
        setFont(new Font("Aril", Font.BOLD, 14));
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200);
        setLocation(100, 100);
        setTitle("Huffmann Compression");

        Container cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.setBackground(Color.WHITE);
        JButton myButton = new JButton("START");
        myButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readLoop();
            }
        });
        cp.add(myButton);
        JButton Browse = new JButton("Browse");
        Browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rVal = c.showOpenDialog(null);
                if(rVal == JFileChooser.APPROVE_OPTION) {
                    path = c.getSelectedFile().getAbsolutePath();
                }
            }
        });
        cp.add(Browse);
        setVisible(true);
    }

    public void start() {
        try {
            ReadHand = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void readLoop() {
        int code = 0;
        String ch;
        start();
        while(true) {
            try {
                code = ReadHand.read();
                if (code == -1)
                    break;
                ch = fromCode_toString(code);
                if (mydata_str == null) {
                    mydata_str = ch;
                } else {
                    mydata_str += ch;
                }
                add_to_list(ch);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try {
            ReadHand.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        sort_my_data(mylist);

        evaluate_code();
        write_To_file();

        code_data_content();
        System.out.println(mydata_str);
        System.out.println(mydata_coded);
        write_Compressed();
        Decompress();
    }

    public void code_data_content() {
        String mySub;
        for(int i=0; i<mydata_str.length(); i++) {
            mySub = mydata_str.substring(i, i+1);
            mySub = getChar_code(mySub);
            if(mydata_coded == null) {
                mydata_coded = mySub;
            } else {
                mydata_coded += mySub;
            }
        }
    }

    public void write_Compressed() {
        File myFile = new File("comp.text");
        FileOutputStream fout;

        try {
            WriteHand = new BufferedWriter(new FileWriter("comp.txt"));
            WriteHand.write(mydata_coded);
            WriteHand.flush();
            WriteHand.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void Decompress() {
        try {
            ReadHand1 = new BufferedReader(new FileReader("table.txt"));
            ReadHand2 = new BufferedReader(new FileReader("comp.txt"));
        } catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }

        String total = null;
        String data_from_file = null;

        try {
            while (true) {
                total = ReadHand1.readLine();
                if (total == null)
                    break;

                if (total != null)
                    list_From_file.add(new data(total.substring(0, 1), total.substring(2)));
            }

            while(true) {
                total = ReadHand2.readLine();
                if (total == null)
                    break;

                if(data_from_file == null) {
                    data_from_file = total;
                } else {
                    data_from_file += total;
                }
            }

            System.out.println(data_from_file);
            int j = 0, index;
            String data_to_file = null;
            for(int i=1; i<=data_from_file.length(); i++) {
                index = isEqualSomething(data_from_file.substring(j, i));
                if(index != -1) {
                    if(data_to_file == null)
                        data_to_file = ((data)list_From_file.get(index)).getChar();
                    else
                        data_to_file = ((data)list_From_file.get(index)).getChar();
                    j = i;
                }
            }

            WriteHand = new BufferedWriter(new FileWriter("decompress.txt"));
            WriteHand.write(data_to_file);
            WriteHand.flush();
            WriteHand.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        display_list_content(list_From_file);
    }

    public int isEqualSomething(String str) {
        for(int i=0; i<list_From_file.size(); i++) {
            if(((data)list_From_file.get(i)).getBinary_Code().equals(str))
                return i;
        }
        return  -1;
    }

    public String getChar_code(String str) {
        myDataList mytable = (myDataList) arrList.get(0);
        for(int i=0; i<mytable.size(); i++) {
            if(((data)mytable.get(i)).getChar().equals(str)) {
                return (((data)mytable.get(i)).getBinary_Code());
            }
        }
        return  "??";
    }

    public void display_list_content(myDataList myLink_list) {
        data my_Data;
        for(int i=0;i<myLink_list.size();i++) {
            my_Data=((data)myLink_list.get(i));
            System.out.println(my_Data.getChar()+" "+my_Data.get_probability()+" "+my_Data.isFlag()+" "
                    +my_Data.getBinary_Code());
        }
    }

    public String fromCode_toString(int code) {
        return Character.toString((char) code);
    }

    public void add_to_list(String ch) {
        for(int i=0;i<mylist.size();i++) {
            if(ch.equals(((data)mylist.get(i)).getChar())) {
                ((data)mylist.get(i)).increment();
                return;
            }
        }
        mylist.add(new data(ch));
    }

    public void sort_my_data(myDataList myLinklist) {
        Collections.sort(myLinklist, my_comparator);
    }

    public static void main(String []args) {
        new Main();
    }

    public void evaluate_code() {
        myDataList linklist = (myDataList) mylist.clone();
        myDataList temp;
        int size = linklist.size();
        arrList.add(linklist.clone());
        while(size>2) {
            ((data)linklist.get(size-2)).append_data(((data)linklist.get(size-1)));
            linklist.removeLast();
            temp=  (( myDataList) linklist.clone());
            ((data)linklist.get(size-2)).setFlag(true);
            setToflagOthers(temp);
            sort_my_data(linklist);
            sort_my_data(temp);
            arrList.add( temp);
            size--;
        }
        doCoding_to_tree();
    }

    public void setToflagOthers(myDataList mylist) {
        for(int i=0; i<mylist.size()-1; i++) {
            ((data)mylist.get(i)).setFlag(false);
        }
    }

    public void doCoding_to_tree() {
        myDataList temp1=((myDataList) arrList.get(arrList.size()-1));
        myDataList temp2;
        data mydata;
        ((data) temp1.get(0)).setBinary_Code("1");
        ((data) temp1.get(1)).setBinary_Code("0");
        for(int i=arrList.size()-1;i>0;i-- ) {
            temp1=((myDataList) arrList.get(i));
            temp2=((myDataList) arrList.get(i-1));
            copmare_and_code(temp1,temp2);
        }
    }

    public int get_Where_the_true_code(myDataList mylist) {
        for(int i=0;i<mylist.size();i++) {
            if(((data)mylist.get(i)).isFlag())
                return i;
        }
        return -1;
    }

    public void copmare_and_code(myDataList temp1,myDataList temp2) {
        for(int j=0;j<temp2.size();j++)
            for(int i=0;i<temp1.size();i++) {
                if ((((data)temp2.get(j)).getChar()).equals(((data)temp1.get(i)).getChar())) {
                    ((data)temp2.get(j)).setBinary_Code(((data)temp1.get(i)).getBinary_Code());

                }

            }
        int postion= get_Where_the_true_code(temp1);
        ((data)temp2.get(temp2.size()-2)).setBinary_Code(((data)temp1.get(postion)).getBinary_Code()+"1");
        ((data)temp2.get(temp2.size()-1)).setBinary_Code(((data)temp1.get(postion)).getBinary_Code()+"0");
    }

    public void write_To_file() {
        try {
            WriteHand = new BufferedWriter(new FileWriter("table.txt"));
            myDataList mytable = (myDataList) arrList.get(0);

            for(int i=0; i<mytable.size(); i++) {
                WriteHand.write(((data)mytable.get(i)).getChar());
                WriteHand.write(" ");
                WriteHand.write(((data)mytable.get(i)).getBinary_Code());
                WriteHand.newLine();
            }
            WriteHand.flush();
            WriteHand.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    short substring_to_int(String str) {
        short b = 0;
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i) == '1') {
                b += (byte)Math.pow(2,i);
            }
        }
        return  b;
    }

    private BufferedReader ReadHand;
    private BufferedReader ReadHand1;
    private BufferedReader ReadHand2;
    private data_Comparator my_comparator = new data_Comparator();
    private myDataList mylist = new myDataList();
    private myDataList arrList = new myDataList();
    private BufferedWriter WriteHand;
    private String mydata_str;
    private String mydata_coded;
    private String path;
    private myDataList list_From_file = new myDataList();
    private  JFileChooser c = new JFileChooser();
}
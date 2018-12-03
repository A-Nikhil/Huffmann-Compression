package huff;

@SuppressWarnings("ALL")
class data
{
    data(String ch)
    {
        this.ch=ch;
        probability=1;
    }
    data(String ch,int prob,boolean bool)
    {
        this.ch=ch;
        probability=prob;
        flag=bool;

    }
    data(String ch,String code)
    {
        this.ch=ch;
        binary_Code=code;
    }
    void increment()
    {
        probability++;
    }
    int get_probability()
    {
        return probability;
    }
    String getChar()
    {
        return ch;
    }
    void append_data(data d)
    {
        ch+=d.getChar();
        probability+=d.get_probability();
        flag=true;
    }

    public void setBinary_Code(String binary_Code) {
        this.binary_Code = binary_Code;
    }

    public String getBinary_Code() {
        return binary_Code;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private int probability;

    protected Object clone() throws CloneNotSupportedException
    {
        return new data(ch,probability,flag);
    }
    private String ch;
    private String binary_Code;
    private boolean flag;
}
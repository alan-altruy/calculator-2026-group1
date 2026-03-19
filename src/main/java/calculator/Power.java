package calculator;

import java.util.List;

public class Power extends Operation {

    public Power(List<Expression> elist) throws IllegalConstruction {
        super(elist);
        symbol = "**";
        neutral = 1;
    }

    public Power(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "**";
        neutral = 1;
    }

    @Override
    public int op(int l, int r) {
        return (int) Math.pow(l, r);
    }
    
    @Override
    public int getPrecedence() {
        return 3;
    }
}

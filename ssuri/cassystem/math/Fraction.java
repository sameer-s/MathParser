package ssuri.cassystem.math;

import java.math.BigInteger;

public class Fraction
{
    public BigInteger numerator, denominator;

    public Fraction(long numerator, long denominator)
    {
        this(numerator, BigInteger.valueOf(denominator));
    }

    public Fraction(long numerator, BigInteger denominator)
    {
        this(BigInteger.valueOf(numerator), denominator);
    }

    public Fraction(BigInteger numerator, BigInteger denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }
    
    public static Fraction multiply(Fraction f1, Fraction f2)
    {
        Fraction mult = new Fraction(f1.numerator.multiply(f2.numerator), f1.denominator.multiply(f2.denominator));
        mult.reduce();
        return mult;
    }
    
    public void reduce()
    {
        BigInteger gcd = numerator.gcd(denominator);
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
    }

    public int compareTo(Fraction other)
    {
        return numerator.multiply(other.denominator).compareTo(other.numerator.multiply(denominator));   
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((denominator == null) ? 0 : denominator.hashCode());
        result = prime * result + ((numerator == null) ? 0 : numerator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Fraction other = (Fraction) obj;
        if (denominator == null)
        {
            if (other.denominator != null) return false;
        }
        else if (!denominator.equals(other.denominator)) return false;
        if (numerator == null)
        {
            if (other.numerator != null) return false;
        }
        else if (!numerator.equals(other.numerator)) return false;
        return true;
    }
}

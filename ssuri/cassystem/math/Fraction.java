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

    public static Fraction add(Fraction f1, Fraction f2)
    {
        Fraction add = new Fraction(f1.numerator.multiply(f2.denominator).add(f2.numerator.multiply(f1.denominator)), f1.denominator.multiply(f2.denominator));
        add.reduce();
        return add;
    }

    public static Fraction multiply(Fraction f1, Fraction f2)
    {
        Fraction mult = new Fraction(f1.numerator.multiply(f2.numerator), f1.denominator.multiply(f2.denominator));
        mult.reduce();
        return mult;
    }

    public static Fraction reciprocal(Fraction f)
    {
        return new Fraction(f.denominator, f.numerator);
    }

    public Fraction reduce()
    {
        BigInteger gcd = numerator.gcd(denominator);
        BigInteger newNumerator = numerator.divide(gcd);
        BigInteger newDenominator = denominator.divide(gcd);
        return new Fraction(newNumerator, newDenominator);
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

    public boolean isZero()
    {
        return numerator.equals(BigInteger.ZERO);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;

        if(!(obj instanceof Fraction))
        {
            return false;
        }

        Fraction other = (Fraction) obj;

        if(other.numerator.equals(numerator) && other.denominator.equals(denominator))
        {
            return true;
        }

        return numerator.multiply(other.denominator).equals(denominator.multiply(other.numerator));
    }
}

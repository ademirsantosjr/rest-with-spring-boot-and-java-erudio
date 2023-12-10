package br.com.erudio.erudioapi.controllers;

import br.com.erudio.erudioapi.utils.NumberConverter;
import br.com.erudio.erudioapi.exceptions.UnsupportedMathOperationsException;
import br.com.erudio.erudioapi.math.SimpleMath;
import br.com.erudio.erudioapi.utils.NumberVerifier;
import org.springframework.web.bind.annotation.*;

@RestController
public class MathController {

    private final SimpleMath math = new SimpleMath();

    @RequestMapping(
            value = "/sum/{numberOne}/{numberTwo}",
            method = RequestMethod.GET
    )
    public Double sum(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo) {
        if (!NumberVerifier.isNumeric(numberOne) || !NumberVerifier.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationsException("It is not a number. Please inform a number type.");
        }
        return math.sum(NumberConverter.toDouble(numberOne), NumberConverter.toDouble(numberTwo));
    }

    @RequestMapping(
            value = "/subtraction/{numberOne}/{numberTwo}",
            method = RequestMethod.GET
    )
    public Double subtraction(@PathVariable(value = "numberOne") String numberOne,
                              @PathVariable(value = "numberTwo") String numberTwo) {
        if (!NumberVerifier.isNumeric(numberOne) || !NumberVerifier.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationsException("It is not a number. Please inform a number type.");
        }
        return math.subtract(NumberConverter.toDouble(numberOne), NumberConverter.toDouble(numberTwo));
    }

    @RequestMapping(
            value = "/multiplication/{numberOne}/{numberTwo}",
            method = RequestMethod.GET
    )
    public Double multiplicate(@PathVariable(value = "numberOne") String numberOne,
                               @PathVariable(value = "numberTwo") String numberTwo) {
        if (!NumberVerifier.isNumeric(numberOne) || !NumberVerifier.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationsException("It is not a number. Please inform a number type.");
        }
        return math.multiplicate(NumberConverter.toDouble(numberOne), NumberConverter.toDouble(numberTwo));
    }

    @RequestMapping(
            value = "/division/{numberOne}/{numberTwo}",
            method = RequestMethod.GET
    )
    public Double divide(@PathVariable(value = "numberOne") String numberOne,
                         @PathVariable(value = "numberTwo") String numberTwo) {
        if (!NumberVerifier.isNumeric(numberOne) || !NumberVerifier.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationsException("It is not a number.");
        }
        if (NumberVerifier.isZero(numberTwo)) {
            throw new UnsupportedMathOperationsException("Division by zero.");
        }
        return math.divide(NumberConverter.toDouble(numberOne), NumberConverter.toDouble(numberTwo));
    }

    @RequestMapping(
            value = "/mean/{numberOne}/{numberTwo}",
            method = RequestMethod.GET
    )
    public Double mean(@PathVariable(value = "numberOne") String numberOne,
                       @PathVariable(value = "numberTwo") String numberTwo) {
        if (!NumberVerifier.isNumeric(numberOne) || !NumberVerifier.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationsException("It is not a number.");
        }
        return math.mean(NumberConverter.toDouble(numberOne), NumberConverter.toDouble(numberTwo));
    }

    @RequestMapping(
            value = "/squareRoot/{numberOne}",
            method = RequestMethod.GET
    )
    public Double squareRoot(@PathVariable(value = "numberOne") String numberOne) {
        if (!NumberVerifier.isNumeric(numberOne)) {
            throw new UnsupportedMathOperationsException("It is not a number.");
        }
        return math.squareRoot(NumberConverter.toDouble(numberOne));
    }

}

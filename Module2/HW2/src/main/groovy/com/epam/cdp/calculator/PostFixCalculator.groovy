package com.epam.cdp.calculator

class PostFixCalculator implements IPostFixCalculator {

    private PostFixConverter postFixConverter
    private Deque<Double> stack = new ArrayDeque<>()

    PostFixCalculator() {
        postFixConverter = new PostFixConverter()
    }

    @Override
    String calculate(String infix) {
        List<String> expression = postFixConverter.convertExpression(infix)
        return result(expression)
    }


    def plus(double value) {
        this.stack.addLast(value)
    }

    private String result(List<String> expression) {
        def size = expression.&size

        for (int index = 0; index != size(); ++index) {
            if (Character.isDigit(expression.get(index).charAt(0))) {
                this + Double.parseDouble(expression.get(index))
            } else {
                double tempResult = 0
                double temp

                switch (expression.get(index)) {
                    case "+":
                        temp = stack.removeLast()
                        tempResult = stack.removeLast() + temp
                        break

                    case "-":
                        temp = stack.removeLast()
                        tempResult = stack.removeLast() - temp
                        break

                    case "*":
                        temp = stack.removeLast()
                        tempResult = stack.removeLast() * temp
                        break

                    case "/":
                        temp = stack.removeLast()
                        tempResult = stack.removeLast() / temp
                        if (Double.isInfinite(tempResult)) {
                            throw new ArithmeticException("Cannot calculate the expression. Cause: division to 0.")
                        }
                        break
                    default:
                        break
                }
                stack.addLast(tempResult)
            }
        }
        def result = new BigDecimal(stack.removeLast()).setScale(10, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
        return "${result}"
    }
}

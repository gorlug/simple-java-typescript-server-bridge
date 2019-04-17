declare class Test {
    constructor();
    doSomething(a, b, c);
    static abc;
    gnarf();
}

function Test() { }

Test.prototype.doSomething = function()
{

}

Test.abc = "123";


var test = new Test();
test.doSomething(

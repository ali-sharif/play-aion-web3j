package controllers;

import org.web3j.aion.VirtualMachine;
import org.web3j.aion.crypto.Ed25519KeyPair;
import org.web3j.aion.protocol.Aion;
import org.web3j.aion.tx.AionTransactionManager;
import org.web3j.aion.tx.gas.AionGasProvider;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.TransactionManager;
import play.mvc.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    // Dummy private key. Use the right private key and endpoint here.
    private static String NODE_ENDPOINT = "http://127.0.0.1:8545";
    private static String PRIVATE_KEY = "0xe438f2d2cc3856255cae3d6f3c32cb7770924a1e76353916940a1d466c56293f47974b53934661fab01bbcc878beb5db2c319e6fb2815be07ce3b2d1ea359ef0";

    private static final Aion aion = Aion.build(new HttpService(NODE_ENDPOINT));

    private static final TransactionManager manager = new AionTransactionManager(
            aion, new Ed25519KeyPair(PRIVATE_KEY), VirtualMachine.AVM
    );

    public Result index() throws Exception {
        // Test web3j-aion interactions here.
        // This assumes that you have an Aion kernel running AVM v1.4, rpc enabled

        // Deploy contract
        final Counter deployedContract = Counter.deploy(aion, manager, AionGasProvider.INSTANCE, 1).send();
        System.out.println("Tx Receipt:"+ deployedContract.getTransactionReceipt());
        System.out.println("Contract Address: " + deployedContract.getContractAddress());

        int ITR = 1;
        for (int i = 1; i <= ITR; i++) {
            // load the contract again, just to check that this feature works
            final Counter loadedContract = Counter.load(deployedContract.getContractAddress(), aion, manager, AionGasProvider.INSTANCE);

            // Check counter
            Integer check = loadedContract.call_getCount().send();
            System.out.println("Current count is: " + check);
            if (check != i)
                System.out.println("Current count expected to be " + i + " but found " + check);


            // Increment counter
            TransactionReceipt tx = loadedContract.send_incrementCounter(1).send();
            if (!tx.isStatusOK())
                System.out.println("Counter could not be incremented");
            System.out.println("Transaction hash: " + tx.getTransactionHash());

            // Check counter again
            Integer recheck = loadedContract.call_getCount().send();

            System.out.println("Current count is: " + recheck);
            if (recheck != i+1)
                System.out.println("Current count expected to be " + i+1 + " but found " + recheck);
        }

        return ok(views.html.index.render());
    }

    public Result explore() {
        return ok(views.html.explore.render());
    }

    public Result tutorial() {
        return ok(views.html.tutorial.render());
    }

}

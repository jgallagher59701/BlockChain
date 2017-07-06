import java.util.ArrayList;

// Block Chain should maintain only limited block nodes to satisfy the functions
// You should not have all the blocks added to the block chain in memory 
// as it would cause a memory overflow.

public class BlockChain {
    public static final int CUT_OFF_AGE = 10;

    private TransactionPool txPool;
    private ArrayList<Block> theBlocks;
    
    /**
     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    public BlockChain(Block genesisBlock) {
        // IMPLEMENT THIS
    		txPool = new TransactionPool();
    		// Start out using a list
    		theBlocks = new ArrayList<Block>();
    }

    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
        // IMPLEMENT THIS
    		return theBlocks.get(0);		// FIXME
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        // IMPLEMENT THIS
    		UTXOPool utxoPool = new UTXOPool();
    		return utxoPool;
    }

    /** Get the transaction pool to mine a new block */
    public TransactionPool getTransactionPool() {
        // IMPLEMENT THIS
    		return txPool;
    }

    /**
     * Add {@code block} to the block chain if it is valid. For validity, all transactions should be
     * valid and block should be at {@code height > (maxHeight - CUT_OFF_AGE)}.
     * 
     * <p>
     * For example, you can try creating a new block over the genesis block (block height 2) if the
     * block chain height is {@code <=
     * CUT_OFF_AGE + 1}. As soon as {@code height > CUT_OFF_AGE + 1}, you cannot create a new block
     * at height 2.
     * 
     * @return true if block is successfully added; false if it is not or if it is a 'genesis' 
     * block (presents a null hash).
     */
    public boolean addBlock(Block block) {
        // IMPLEMENT THIS
    		if (block.getHash() == null)
    			return false;
    		
    		// Check if block is valid - checking that the txs form a valid set is enough
    		
    		return true;		// FIXME
    }

    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
        // IMPLEMENT THIS
    }
}
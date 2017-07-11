
import java.util.ArrayList;
import java.util.HashMap;

// From teh assignment:
// Block Chain should maintain only limited block nodes to satisfy the functions
// You should not have all the blocks added to the block chain in memory 
// as it would cause a memory overflow.
//
// I kept the whole blockchain in memory. Older nodes could be dropped once they
// had unclaimed outputs, but I never needed to code that.

public class BlockChain {
    public static final int CUT_OFF_AGE = 10;

    /// The current pool of transactions
    private TransactionPool txPool;
    
    /// Mapping between Block's hash and the Node that holds it
    private HashMap<byte[], Node> nodeMap;
    
    /// The Node with the greatest height
    private Node maxHeightBlockNode;
    
    private class Node {
    		Block block;
    		ArrayList<Node> children;
    		
    		/// Unclaimed outputs associated with this block
    		UTXOPool utxoPool;
    		
    		/// Height of the block chain at this point
    		int height;
    		
    		/**
    		 * Add a new Block to the BlockChain. This also updates the global
    		 * Node Map.
    		 * @param block
    		 * @param utxoPool Unclaimed tx outputs, init'd to the block's coinbase outputs
    		 */
    		public Node(Block block, UTXOPool utxoPool) {
    			this.block = block;
    			this.children = new ArrayList<Node>();
    			this.utxoPool = utxoPool;
    			
    			// Genesis block
    			if (block.getPrevBlockHash() == null) {
    				this.height = 1;
        			maxHeightBlockNode = this;
    			}
    			else {
    				Node parent = nodeMap.get(block.getPrevBlockHash());
    				this.height = parent.height + 1;
    				parent.children.add(this);
    			}
    		}
    		
    		/** 
    		 * Get the unclaimed transaction outputs for the block associated
    		 * with this node of the BlockChain 
    		 */
    		UTXOPool getUTXOPool() {
    			return utxoPool;
    		}
    }
        
    private void addCoinbaseToUTXOPool(Block block, UTXOPool utxoPool) {
        Transaction coinbase = block.getCoinbase();
        for (int i = 0; i < coinbase.numOutputs(); i++) {
            Transaction.Output out = coinbase.getOutput(i);
            UTXO utxo = new UTXO(coinbase.getHash(), i);
            utxoPool.addUTXO(utxo, out);
        }
    }
    
    /**
     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    public BlockChain(Block genesisBlock) {
        // IMPLEMENT THIS
    		txPool = new TransactionPool();
    	    nodeMap = new HashMap<byte[], Node>();
    	    
    	    UTXOPool utxoPool = new UTXOPool();
    	    addCoinbaseToUTXOPool(genesisBlock, utxoPool);

    	    Node newNode = new Node(genesisBlock, utxoPool);
    	    nodeMap.put(genesisBlock.getHash(), newNode);
    	    maxHeightBlockNode = newNode;  
    }

    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
        // IMPLEMENT THIS
    		return maxHeightBlockNode.block;
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        // IMPLEMENT THIS
    		return new UTXOPool(maxHeightBlockNode.getUTXOPool());
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
    		
    		if (block.getPrevBlockHash() == null)
    			return false;
    		
    		// Get the parent block node
    		Node parentNode = nodeMap.get(block.getPrevBlockHash());
    		// This should probably be an exception. TODO?
    		if (parentNode == null)
    			return false;
    		
    		if (!(parentNode.height + 1 > maxHeightBlockNode.height - CUT_OFF_AGE))
    			return false;
    		
    		// Build a TxHandler using a copy of the UTXOPool of the parent block 
    		TxHandler txHandler = new TxHandler(new UTXOPool(parentNode.getUTXOPool()));
    		
    		// Check if block is valid - checking that the txs form a valid set is enough
    		ArrayList<Transaction> blockTxs = block.getTransactions();
    		
    		Transaction[] validTxs = txHandler.handleTxs(blockTxs.toArray(new Transaction[blockTxs.size()]));
    		if (validTxs.length != blockTxs.size())
    			return false;

    		UTXOPool utxoPool = txHandler.getUTXOPool();
    		addCoinbaseToUTXOPool(block, utxoPool);
    		Node newNode = new Node(block, utxoPool);
    		nodeMap.put(block.getHash(), newNode);
    		
    		if (newNode.height > maxHeightBlockNode.height)
    			maxHeightBlockNode = newNode;

    		return true;	
    }

    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
        // IMPLEMENT THIS
    		txPool.addTransaction(tx);
    }
}
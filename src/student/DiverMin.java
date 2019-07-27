package student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.FindState;
import game.FleeState;
import game.Node;
import game.NodeStatus;
import game.SewerDiver;

// NetID-it233, acs299
//Implemented solution given for A6 in paths.java

public class DiverMin extends SewerDiver {

	/** Get to the ring in as few steps as possible. Once you get there, <br>
	 * you must return from this function in order to pick<br>
	 * it up. If you continue to move after finding the ring rather <br>
	 * than returning, it will not count.<br>
	 * If you return from this function while not standing on top of the ring, <br>
	 * it will count as a failure.
	 *
	 * There is no limit to how many steps you can take, but you will receive<br>
	 * a score bonus multiplier for finding the ring in fewer steps.
	 *
	 * At every step, you know only your current tile's ID and the ID of all<br>
	 * open neighbor tiles, as well as the distance to the ring at each of <br>
	 * these tiles (ignoring walls and obstacles).
	 *
	 * In order to get information about the current state, use functions<br>
	 * currentLocation(), neighbors(), and distanceToRing() in state.<br>
	 * You know you are standing on the ring when distanceToRing() is 0.
	 *
	 * Use function moveTo(long id) in state to move to a neighboring<br>
	 * tile by its ID. Doing this will change state to reflect your new position.
	 *
	 * A suggested first implementation that will always find the ring, but <br>
	 * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
	 * Some modification is necessary to make the search better, in general. */
	@Override
	public void find(FindState state) {
		// TODO : Find the ring and return.
		// DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
		// Instead, write your method elsewhere, with a good specification,
		// and call it from this one.

		Set<Long> reached= new HashSet<Long>();	// Set for maintaining reached or visited nodes

		updateddepfirsearch(state, reached);	// method called from inside main() </br> Responsible for finding the
												// ring and returning if found.

	}

	/** Helper function for [find] responsible for finding the ring and returning true if found else
	 * false. */
	public static boolean updateddepfirsearch(FindState state, Set<Long> group) {

		if (state.distanceToRing() == 0) { 	// found the ring
										   	// base case defined for recursive call
			return true;
		}

		long id= state.currentLocation(); 	// id contains the id of current state

		group.add(id); 						// add id to set of visited state namely "group"

		for (int i= 0; i < 7; i++ ) {

			NodeStatus n= ringer(state, group); // helper function returns neighbor with min dist to ring

			if (n == null) {

				return false; 					// neighbor is null then return false
			}

			if (!group.contains(n.getId())) {
				// if neighbor's ID with minimum distance is not in the visited set

				state.moveTo(n.getId()); 		// DiverMin will move to neighbor

				if (updateddepfirsearch(state, group) != true) { // check for whether ring still not found (recursion)

					state.moveTo(id); 			// DiverMin moves to id if ring not found;

				}

				else {
					return true;
				}					 // returns true if visited state contains the neighbor
				 					 // with min distance ID
			}
		}

		return false;

	}

	/** Helper function for depfirsearch. Return adjacent/neighbor node with least or min distance to
	 * the ring. */

	public static NodeStatus ringer(FindState state, Set<Long> group) {

		NodeStatus nil= null; 							// nil variable of type NodeStatus is responsible for keeping
							  							// track of neighbors with least distance to ring

		int distance= Integer.MAX_VALUE;				// upper boundary on distance; implemented this in order to
														// avoid returning a smaller distance.

		for (NodeStatus w : state.neighbors()) {		// look over entire neighbors of state.

			if (w.getDistanceToTarget() < distance && !group.contains(w.getId())) { // additional check incorporated for
																				    // whether id of neighbor w is not
																				    // in reached / visited state

				nil= w;								 // nil= neighbor with the least distance to the ring

				distance= w.getDistanceToTarget(); 	// distance= min distance to ring

			}
		}
		return nil; 								// node returned with min distance to ring
	}

	/** Flee the sewer system before the steps are all used, trying to <br>
	 * collect as many coins as possible along the way. Your solution must ALWAYS <br>
	 * get out before the steps are all used, and this should be prioritized above<br>
	 * collecting coins.
	 *
	 * You now have access to the entire underlying graph, which can be accessed<br>
	 * through FleeState. currentNode() and getExit() will return Node objects<br>
	 * of interest, and getNodes() will return a collection of all nodes on the graph.
	 *
	 * You have to get out of the sewer system in the number of steps given by<br>
	 * getStepsRemaining(); for each move along an edge, this number is <br>
	 * decremented by the weight of the edge taken.
	 *
	 * Use moveTo(n) to move to a node n that is adjacent to the current node.<br>
	 * When n is moved-to, coins on node n are automatically picked up.
	 *
	 * You must return from this function while standing at the exit. Failing <br>
	 * to do so before steps run out or returning from the wrong node will be<br>
	 * considered a failed run.
	 *
	 * Initially, there are enough steps to get from the starting point to the<br>
	 * exit using the shortest path, although this will not collect many coins.<br>
	 * For this reason, a good starting solution is to use the shortest path to<br>
	 * the exit. */
	@Override
	public void flee(FleeState state) {
		// TODO: Get out of the sewer system before the steps are used up.
		// DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
		// with a good specification, and call it from this one.

		flee2(state);// method called from inside main().

	}

	/** Helper function responsible for Min to flee sewer system with max coins. We implemented the
	 * concept of ratio between highest number of coins to distance. For this method we define a
	 * recursive call wherein the node with highest coin to distance ratio is selected and moved to with
	 * the shortest path possible; ending at the time when DiverMin reaches the exit. */

	public void flee2(FleeState state) {

		if (state.currentNode() == state.getExit()) return; // returns in case of DiverMin having reached the exit().
														    // This acts as the base case for the recursive call.

		double Ratio_MAX= 0;							 // initialize the maximum ratio of nodes with highest coins to
														 // distance.

		Node nodes= null;								// variable for keeping track of the node in for-each loop over
						 								// [everynode]

		List<Node> everynode= new ArrayList<Node>(); // [everynode] contains all the nodes in form of an ArrayList.

		for (Node k : state.allNodes()) {
			if (k != state.currentNode()) { // Add all values except identifier for tile the current node is standing on
										    // from state.currentNode() to List [everynode]
				everynode.add(k);
			}
		}

		for (Node k : everynode) {

			List<Node> trail= Paths.shortest(state.currentNode(), k); // trail= shortest path to a node.

			double need_steps= Paths.pathSum(trail) + Paths.pathSum(Paths.shortest(k, state.getExit()));

			double ratio= k.getTile().coins() / Paths.pathSum(trail); // ratio= coins to distance ratio

			if (ratio > Ratio_MAX && need_steps < state.stepsLeft()) {

				nodes= k; 											// k assigned to [nodes] if steps still left

				Ratio_MAX= ratio;									// update value of [Ratio_MAX]

			}
		}

		if (nodes == null) {

			List<Node> path= Paths.shortest(state.currentNode(), state.getExit());

			state.moveTo(path.get(1));
		}

		else {															// in case of nodes!=null
			List<Node> new_trail= Paths.shortest(state.currentNode(), nodes); // [new_trail]= shortest path to [nodes]

			new_trail.remove(0);  										// remove current_node

			for (Node i : new_trail) {

				state.moveTo(i); 										// in this case will move to node with the
								 										// highest ratio in [new_trail]
			}

		}

		flee2(state);												 // recursive implementation
	}

}

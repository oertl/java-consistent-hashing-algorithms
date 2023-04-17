package ch.supsi.dti.isin.consistenthash.anchor;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.nerd4j.utils.lang.RequirementFailure;

import ch.supsi.dti.isin.cluster.Node;
import ch.supsi.dti.isin.cluster.SimpleNode;
import ch.supsi.dti.isin.consistenthash.ConsistentHashContract;

/**
 * Test suite for the class {@link AnchorHash}.
 * 
 * @author Massimo Coluzzi
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AnchorHashTests implements ConsistentHashContract<AnchorHash>
{

    /* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */

    /**
     * {@inheritDoc}
     */
    @Override
    public AnchorHash sampleValue( Collection<? extends Node> nodes )
    {

        return sampleValue( nodes, nodes.size() << 1 );

    }


    /**
     * Creates a new {@link AnchorHash} with the
     * given size and capacity.
     * 
     * @param nodes the initial nodes
     * @param capacity the overall capacity
     */
    public AnchorHash sampleValue( Collection<? extends Node> nodes, int capacity )
    {

        return new AnchorHash( nodes, capacity );

    }

    
    /* ************** */
    /*  TEST METHODS  */
    /* ************** */

    
    @ParameterizedTest
    @NullAndEmptySource
    public void the_cluster_must_have_at_least_one_node( List<Node> nodes )
    {

        assertThrows(
            RequirementFailure.class,
            () -> new AnchorHash( nodes, 10 )
        );
        
    }

    @Test
    public void initial_nodes_cannot_be_null()
    {

        assertThrows(
            RequirementFailure.class,
            () -> new AnchorHash( Collections.singletonList(null), 10 )
        );
        
    }

    @Test
    public void initial_nodes_cannot_be_duplicated()
    {

        final List<Node> nodes = IntStream.of( 1, 1 )
            .mapToObj( SimpleNode::of )
            .collect( toList() );

        assertThrows(
            RequirementFailure.class,
            () -> new AnchorHash( nodes, 10 )
        );
        
    }

    @Test
    public void the_cluster_size_cannot_be_greater_than_the_overall_capacity()
    {

        final List<Node> nodes = IntStream.of( 1, 2 )
            .mapToObj( SimpleNode::of )
            .collect( toList() );

        assertThrows(
            RequirementFailure.class,
            () -> sampleValue( nodes, 1 )
        );
        
    }

    @Test
    public void adding_nodes_cannot_exceed_capacity()
    {

        final List<Node> nodes = IntStream.of( 1, 2, 3, 4, 5 )
            .mapToObj( SimpleNode::of )
            .collect( toList() );

        final AnchorHash anchorHash = sampleValue( nodes, 6 );
        
        assertDoesNotThrow( () -> anchorHash.addNodes( Collections.singleton(SimpleNode.of(100))) );
        assertThrows( RequirementFailure.class, () -> anchorHash.addNodes(Collections.singleton(SimpleNode.of(101))) );

    }

}

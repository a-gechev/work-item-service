/**
 * The edge of the Work Items context: everything that translates between the outside world and the
 * application layer.
 *
 * <p>Structure only. Adapters live in the leaf packages below, each named for what it speaks.
 * Dependencies run inwards: an adapter may depend on {@code application} and {@code domain}; neither
 * may depend on an adapter.
 */
package io.github.agechev.workitem.workitems.adapters;

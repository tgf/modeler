/*!
 * PENTAHO CORPORATION PROPRIETARY AND CONFIDENTIAL
 *
 * Copyright 2002 - 2014 Pentaho Corporation (Pentaho). All rights reserved.
 *
 * NOTICE: All information including source code contained herein is, and
 * remains the sole property of Pentaho and its licensors. The intellectual
 * and technical concepts contained herein are proprietary and confidential
 * to, and are trade secrets of Pentaho and may be covered by U.S. and foreign
 * patents, or patents in process, and are protected by trade secret and
 * copyright laws. The receipt or possession of this source code and/or related
 * information does not convey or imply any rights to reproduce, disclose or
 * distribute its contents, or to manufacture, use, or sell anything that it
 * may describe, in whole or in part. Any reproduction, modification, distribution,
 * or public display of this information without the express written authorization
 * from Pentaho is strictly prohibited and in violation of applicable laws and
 * international treaties. Access to the source code contained herein is strictly
 * prohibited to anyone except those individuals and entities who have executed
 * confidentiality and non-disclosure agreements or other agreements with Pentaho,
 * explicitly covering such access.
 */

package org.pentaho.agilebi.modeler.models.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.pentaho.agilebi.modeler.ModelerException;
import org.pentaho.agilebi.modeler.ModelerWorkspace;
import org.pentaho.agilebi.modeler.models.annotations.ModelAnnotation.SourceType;
import org.pentaho.agilebi.modeler.util.ModelerWorkspaceHelper;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rowell Belen
 */
public class ModelAnnotationTest {

  @Test
  public void testMeasure() {

    CreateMeasure createMeasure = new CreateMeasure();

    List<ModelAnnotation<?>> list = new ArrayList<ModelAnnotation<?>>();
    list.add( new ModelAnnotation<CreateMeasure>( "testColumn", createMeasure ) );

    ModelAnnotation<?> md = list.get( 0 ); // don't know the type

    // check
    assertEquals( md.getField(), "testColumn" );
    assertEquals( md.getType(), ModelAnnotation.Type.CREATE_MEASURE );
    assertEquals( md.getType().description(), "Create Measure" );
  }

  @Test
  public void testAttribute() {

    CreateAttribute createAttribute = new CreateAttribute();

    List<ModelAnnotation<?>> list = new ArrayList<ModelAnnotation<?>>();
    list.add( new ModelAnnotation<CreateAttribute>( "testColumn", createAttribute ) );

    ModelAnnotation<?> md = list.get( 0 ); // don't know the type

    // check
    assertEquals( md.getField(), "testColumn" );
    assertEquals( md.getType(), ModelAnnotation.Type.CREATE_ATTRIBUTE );
    assertEquals( md.getType().description(), "Create Attribute" );
  }

  @Test
  public void testGetAndFilter() {

    List<ModelAnnotation<?>> annotations = new ArrayList<ModelAnnotation<?>>();
    annotations.add( new ModelAnnotation<CreateMeasure>( "A", new CreateMeasure() ) );
    annotations.add( new ModelAnnotation<CreateAttribute>( "B", new CreateAttribute() ) );
    annotations.add( new ModelAnnotation<CreateMeasure>( "E", new CreateMeasure() ) );

    assertEquals( ModelAnnotation.getMeasures( annotations ).size(), 2 );
    assertEquals( ModelAnnotation.getAttributes( annotations ).size(), 1 );
  }

  @Test
  public void testAppliesAllAnnotationsToWorkspace() throws Exception {
    final ModelerWorkspace modelerWorkspace = new ModelerWorkspace( new ModelerWorkspaceHelper( "" ) );
    AnnotationType annotationType = new AnnotationType() {
      @Override
      public boolean apply( final ModelerWorkspace workspace, final String column ) {
        assertSame( workspace, modelerWorkspace );
        assertEquals( "amount", column );
        return true;
      }

      @Override public ModelAnnotation.Type getType() {
        return null;
      }

      @Override public String getSummary() {
        return "";
      }

      @Override
      public boolean apply( Document schema, String field ) throws ModelerException {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public String getName() {
        return "";
      }

      @Override
      public void validate() throws ModelerException {}
    };
    ModelAnnotation<AnnotationType> modelAnnotation = new ModelAnnotation<AnnotationType>();
    modelAnnotation.setField( "amount" );
    modelAnnotation.setAnnotation( annotationType );
    modelAnnotation.setSourceType( SourceType.StreamField );
    modelAnnotation.apply( modelerWorkspace );
  }
}

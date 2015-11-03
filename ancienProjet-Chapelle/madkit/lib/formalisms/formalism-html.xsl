<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:template match="/">
    <HTML>
      <BODY bgcolor="white">
	<xsl:apply-templates/>
      </BODY>
    </HTML>
  </xsl:template>

  <xsl:template match="formalism">
    <H1>Formalism "<xsl:value-of select="@name"/>"</H1>
    <xsl:value-of select="@description"/>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="formalism-info">
    <H2>Authors</H2>
    <ul>
      <xsl:for-each select="author">
	<LI><xsl:apply-templates/></LI>
      </xsl:for-each>
    </ul>
    <HR />
  </xsl:template>

  <xsl:template match="node-types">
    <H2>Node Types</H2>
 <BLOCKQUOTE>
    <xsl:apply-templates/>
 </BLOCKQUOTE>
    <HR />
  </xsl:template>

  <xsl:template match="connector-desc">
    <H3>Connector "<xsl:value-of select="@name"/>"
      (<xsl:value-of select="@mode"/>)</H3>
    <xsl:if test="@description">
      <B>Description: </B> <xsl:value-of select="@description"/>
      <xsl:attribute name="width">100%</xsl:attribute>
      <BR />
    </xsl:if>
    <xsl:if test="@class">	
      This connector is mapped to class
      <TT><xsl:value-of select="@class"/></TT>
      <BR />
    </xsl:if>
    <xsl:apply-templates/>
  </xsl:template>


  <xsl:template match="node-desc">
    <A>
    <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
    <H3>Node "<xsl:value-of select="@name"/>" </H3>
    </A>
    <xsl:if test="@description">
      <B>Description: </B> <xsl:value-of select="@description"/>
      <xsl:attribute name="width">100%</xsl:attribute>
      <BR />
    </xsl:if>
    <xsl:if test="@class">	
      This node is mapped to class
      <TT><xsl:value-of select="@class"/></TT>
      <BR />
    </xsl:if>
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="arrow-desc">
    <H3>Arrow "<xsl:value-of select="@name"/>"</H3>
    <xsl:if test="@description">
      <B>Description: </B> <xsl:value-of select="@description"/>
      <xsl:attribute name="width">100%</xsl:attribute>
      <BR />
    </xsl:if>
    <xsl:if test="@class">	
      This arrow is mapped to class
      <TT><xsl:value-of select="@class"/></TT>
      <BR />
    </xsl:if>
    <xsl:if test="@from">	
      This arrow must have as origin:
      <A><xsl:attribute name="href">#<xsl:value-of select="@name"/></xsl:attribute>
      <TT><xsl:value-of select="@from"/></TT>
      </A>
      <BR />
    </xsl:if>
    <xsl:if test="@to">	
      This arrow must have as destination:
      <A><xsl:attribute name="href">#<xsl:value-of select="@name"/></xsl:attribute>
      <TT><xsl:value-of select="@to"/></TT>
      </A>
      <BR />
    </xsl:if>

    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="connector">
    <B>Connector "<xsl:value-of select="@name"/>"</B>
   	is instance of connector type:
      <A><xsl:attribute name="href">#<xsl:value-of select="@type"/></xsl:attribute>
      <xsl:value-of select="@type"/>
      </A>
     <BR />
    <xsl:apply-templates/>
  </xsl:template>


  <xsl:template match="icon">
    Icon for element bar: <TT><xsl:value-of select="@url"/></TT>
    <BR />
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="graphic-element">
    <BLOCKQUOTE>
      <H4>Graphic element: <TT><xsl:value-of select="@class"/></TT>
      </H4>
      <xsl:apply-templates/>
    </BLOCKQUOTE>
  </xsl:template>

  <xsl:template match="property">
    Property: <xsl:value-of select="@name"/> =
    <I> <xsl:apply-templates/> </I>
    <BR />
  </xsl:template>

  <xsl:template match="module">
    <BLOCKQUOTE>
      <H4>Module</H4>
      <B>Type:</B> <xsl:value-of select="@type"/>
      <BR /><B>Layout:</B> <xsl:value-of select="@layout"/>
      <P />
      <xsl:apply-templates/>
    </BLOCKQUOTE>
  </xsl:template>


  <xsl:template match="arrow-types">
    <H2>Arrow Types</H2>
 <BLOCKQUOTE>
    <xsl:apply-templates/>
 </BLOCKQUOTE>
    <HR />
  </xsl:template>

  <xsl:template match="connector-types">
    <H2>Connector Types</H2>
  <BLOCKQUOTE>
   <xsl:apply-templates/>
 </BLOCKQUOTE>
    <HR />
  </xsl:template>

</xsl:stylesheet>
